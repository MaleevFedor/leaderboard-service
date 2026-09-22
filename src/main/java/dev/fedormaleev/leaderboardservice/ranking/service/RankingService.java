package dev.fedormaleev.leaderboardservice.ranking.service;

import dev.fedormaleev.leaderboardservice.ranking.api.LeaderboardElement;
import dev.fedormaleev.leaderboardservice.ranking.api.LeaderboardResponse;
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

        log.info("Leaderboard successfully updated");
    }

    public LeaderboardResponse getTop(String leaderboardId, long limit){
        Set<ZSetOperations.TypedTuple<String>> tuples = redisLeaderboardRepository.getTop(leaderboardId, limit);

        List<LeaderboardElement> elements = new ArrayList<>();

        int rank = 1;

        for (var elem : tuples) {
            elements.add(new LeaderboardElement(elem.getValue(), elem.getScore().longValue(), rank, Instant.now()));
            rank++;
        }

        return new LeaderboardResponse(leaderboardId, elements);
    }
}
