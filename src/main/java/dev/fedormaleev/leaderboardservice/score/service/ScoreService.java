package dev.fedormaleev.leaderboardservice.score.service;

import dev.fedormaleev.leaderboardservice.ranking.persistence.LeaderboardScoreEntity;
import dev.fedormaleev.leaderboardservice.ranking.persistence.LeaderboardScoreId;
import dev.fedormaleev.leaderboardservice.ranking.persistence.LeaderboardScoreRepository;
import dev.fedormaleev.leaderboardservice.ranking.service.RankingService;
import dev.fedormaleev.leaderboardservice.score.api.AddScoreRequest;
import dev.fedormaleev.leaderboardservice.score.api.AddScoreResponse;
import dev.fedormaleev.leaderboardservice.score.persistence.ScoreEventEntity;
import dev.fedormaleev.leaderboardservice.score.persistence.ScoreEventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class ScoreService {

    private final ScoreEventRepository scoreEventRepository;
    private final RankingService rankingService;

    public ScoreService(
            ScoreEventRepository scoreEventRepository,
            RankingService rankingService) {
        this.scoreEventRepository = scoreEventRepository;
        this.rankingService = rankingService;
    }

    @Transactional
    public AddScoreResponse addScore(
            String leaderboardId,
            AddScoreRequest request
    ) {
        Instant now = Instant.now();

        ScoreEventEntity event = new ScoreEventEntity(
                leaderboardId,
                request.userId(),
                request.points(),
                request.occurredAt(),
                now
        );

        scoreEventRepository.save(event);

        rankingService.actualiseLeaderboard(leaderboardId, request.userId(), request.points(), now);

        return new AddScoreResponse(
                event.getLeaderboardId(),
                event.getUserId(),
                event.getPoints(),
                event.getOccurredAt(),
                event.getCreatedAt()
        );
    }
}