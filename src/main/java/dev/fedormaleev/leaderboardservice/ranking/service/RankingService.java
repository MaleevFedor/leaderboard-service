package dev.fedormaleev.leaderboardservice.ranking.service;

import dev.fedormaleev.leaderboardservice.ranking.persistence.LeaderboardScoreEntity;
import dev.fedormaleev.leaderboardservice.ranking.persistence.LeaderboardScoreId;
import dev.fedormaleev.leaderboardservice.ranking.persistence.LeaderboardScoreRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class RankingService {

    private final LeaderboardScoreRepository leaderboardScoreRepository;
    private static final Logger log = LoggerFactory.getLogger(RankingService.class);

    public RankingService(LeaderboardScoreRepository leaderboardScoreRepository){
        this.leaderboardScoreRepository = leaderboardScoreRepository;
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

        log.info("Leaderboard successfully updated");
    }
}
