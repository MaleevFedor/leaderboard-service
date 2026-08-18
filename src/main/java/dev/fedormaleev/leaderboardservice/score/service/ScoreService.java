package dev.fedormaleev.leaderboardservice.score.service;

import dev.fedormaleev.leaderboardservice.ranking.service.RankingService;
import dev.fedormaleev.leaderboardservice.score.api.AddScoreRequest;
import dev.fedormaleev.leaderboardservice.score.api.AddScoreResponse;
import dev.fedormaleev.leaderboardservice.score.persistence.ScoreEventEntity;
import dev.fedormaleev.leaderboardservice.score.persistence.ScoreEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class ScoreService {

    private final ScoreEventRepository scoreEventRepository;
    private final RankingService rankingService;
    private static final Logger log = LoggerFactory.getLogger(ScoreService.class);

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
        log.debug("Adding event: leaderboardId={}, userId={}, points={}",
                leaderboardId, request.userId(), request.points());
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

        log.info("Score event successfully added");

        return new AddScoreResponse(
                event.getLeaderboardId(),
                event.getUserId(),
                event.getPoints(),
                event.getOccurredAt(),
                event.getCreatedAt()
        );
    }
}