package dev.fedormaleev.leaderboardservice.ranking.service;

import dev.fedormaleev.leaderboardservice.common.error.NoUserInLeaderboardException;
import dev.fedormaleev.leaderboardservice.ranking.api.dto.LeaderboardElement;
import dev.fedormaleev.leaderboardservice.ranking.api.dto.LeaderboardResponse;
import dev.fedormaleev.leaderboardservice.ranking.api.dto.RankResponse;
import dev.fedormaleev.leaderboardservice.ranking.persistence.LeaderboardScoreEntity;
import dev.fedormaleev.leaderboardservice.ranking.persistence.LeaderboardScoreId;
import dev.fedormaleev.leaderboardservice.ranking.persistence.LeaderboardScoreRepository;
import dev.fedormaleev.leaderboardservice.ranking.persistence.RedisLeaderboardRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class RankingService {

    private final LeaderboardScoreRepository leaderboardScoreRepository;
    private final RedisLeaderboardRepository redisLeaderboardRepository;
    private static final Logger log = LoggerFactory.getLogger(RankingService.class);

    public RankingService(LeaderboardScoreRepository leaderboardScoreRepository,
                          RedisLeaderboardRepository redisLeaderboardRepository){
        this.leaderboardScoreRepository = leaderboardScoreRepository;
        this.redisLeaderboardRepository = redisLeaderboardRepository;
    }

    @Transactional
    public void actualiseLeaderboard(String leaderboardId, String userId, long points, Instant requestTime){
        log.debug("Adding score: leaderboardId={}, userId={}, points={}", leaderboardId, userId, points);
        LeaderboardScoreId scoreId = new LeaderboardScoreId(leaderboardId, userId);

        LeaderboardScoreEntity leaderboardScore =
                leaderboardScoreRepository.findById(scoreId)
                        .orElseGet(() -> new LeaderboardScoreEntity(
                                leaderboardId,
                                userId,
                                0,
                                requestTime
                        ));

        leaderboardScore.setScore(
                leaderboardScore.getScore() + points
        );

        leaderboardScore.setUpdatedAt(requestTime);

        leaderboardScoreRepository.save(leaderboardScore);

        redisLeaderboardRepository.incrementScore(leaderboardId, userId, points);

        log.info("Leaderboard successfully updated.");
    }

    public LeaderboardResponse getTop(String leaderboardId, long limit){
        Set<ZSetOperations.TypedTuple<String>> tuples = redisLeaderboardRepository.getTop(leaderboardId, limit);

        List<LeaderboardElement> elements = new ArrayList<>();

        int rank = 1;

        for (var elem : tuples) {
            elements.add(new LeaderboardElement(elem.getValue(), elem.getScore().longValue(), rank, Instant.now()));
            rank++;
        }

        log.info("Top of leaderboard id: " + leaderboardId + " fetched.");

        return new LeaderboardResponse(leaderboardId, elements);
    }

    public long getRank(
            String leaderboardId,
            String userId
    ) {
        Long rank = redisLeaderboardRepository.getRank(
                leaderboardId,
                userId
        );

        if (rank == null) {
            throw new NoUserInLeaderboardException(
                    leaderboardId,
                    userId
            );
        }

        return rank + 1;
    }

    public LeaderboardResponse around(String leaderboardId, String userId, long radius){
        long top = getRank(leaderboardId, userId) - radius;
        if (top < 0){
            top = 0;
        }

        long bottom = top + radius * 2;

        Set<ZSetOperations.TypedTuple<String>> tuples = redisLeaderboardRepository.getTop(leaderboardId, bottom);

        List<LeaderboardElement> elements = new ArrayList<>();

        int rank = 1;

        for (var elem : tuples) {
            if (rank >= top) {
                elements.add(new LeaderboardElement(elem.getValue(), elem.getScore().longValue(), rank, Instant.now()));
            }
            rank++;
        }

        log.info("Top of leaderboard id: " + leaderboardId + " fetched.");

        return new LeaderboardResponse(leaderboardId, elements);
    }
}
