package dev.fedormaleev.leaderboardservice.score;

import dev.fedormaleev.leaderboardservice.ranking.service.RankingService;
import dev.fedormaleev.leaderboardservice.score.api.AddScoreRequest;
import dev.fedormaleev.leaderboardservice.score.api.AddScoreResponse;
import dev.fedormaleev.leaderboardservice.score.persistence.ScoreEventEntity;
import dev.fedormaleev.leaderboardservice.score.persistence.ScoreEventRepository;
import dev.fedormaleev.leaderboardservice.score.service.ScoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ScoreServiceTest {

    @Mock
    private ScoreEventRepository scoreEventRepository;

    @Mock
    private RankingService rankingService;

    private ScoreService scoreService;

    @BeforeEach
    void setUp() {
        scoreService = new ScoreService(
                scoreEventRepository,
                rankingService
        );
    }

    @Test
    void shouldSaveScoreEventAndUpdateLeaderboard() {
        Instant occurredAt =
                Instant.parse("2026-08-27T10:00:00Z");

        AddScoreRequest request =
                new AddScoreRequest(
                        "fedor",
                        100,
                        occurredAt
                );

        AddScoreResponse response =
                scoreService.addScore(
                        "dota",
                        request
                );

        ArgumentCaptor<ScoreEventEntity> eventCaptor =
                ArgumentCaptor.forClass(
                        ScoreEventEntity.class
                );

        verify(scoreEventRepository)
                .save(eventCaptor.capture());

        ScoreEventEntity savedEvent =
                eventCaptor.getValue();

        assertThat(savedEvent.getLeaderboardId())
                .isEqualTo("dota");

        assertThat(savedEvent.getUserId())
                .isEqualTo("fedor");

        assertThat(savedEvent.getPoints())
                .isEqualTo(100);

        assertThat(savedEvent.getOccurredAt())
                .isEqualTo(occurredAt);

        verify(rankingService)
                .actualiseLeaderboard(
                        "dota",
                        "fedor",
                        100,
                        savedEvent.getCreatedAt()
                );

        assertThat(response.leaderboardId())
                .isEqualTo("dota");

        assertThat(response.userId())
                .isEqualTo("fedor");

        assertThat(response.points())
                .isEqualTo(100);

        assertThat(response.occurredAt())
                .isEqualTo(occurredAt);
    }
}