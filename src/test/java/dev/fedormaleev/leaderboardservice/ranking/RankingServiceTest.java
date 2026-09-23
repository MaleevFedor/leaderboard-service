package dev.fedormaleev.leaderboardservice.ranking;

import dev.fedormaleev.leaderboardservice.ranking.persistence.LeaderboardScoreEntity;
import dev.fedormaleev.leaderboardservice.ranking.persistence.LeaderboardScoreId;
import dev.fedormaleev.leaderboardservice.ranking.persistence.LeaderboardScoreRepository;
import dev.fedormaleev.leaderboardservice.ranking.persistence.RedisLeaderboardRepository;
import dev.fedormaleev.leaderboardservice.ranking.service.RankingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RankingServiceTest {

    @Mock
    private LeaderboardScoreRepository leaderboardScoreRepository;
    @Mock
    private RedisLeaderboardRepository redisLeaderboardRepository;

    private RankingService rankingService;

    @BeforeEach
    void setUp() {
        rankingService = new RankingService(
                leaderboardScoreRepository,
                redisLeaderboardRepository
        );
    }

    @Test
    void shouldIncrementExistingScore() {
        Instant oldTime =
                Instant.parse("2026-08-26T10:00:00Z");

        Instant requestTime =
                Instant.parse("2026-08-27T10:00:00Z");

        LeaderboardScoreId id =
                new LeaderboardScoreId(
                        "dota",
                        "fedor"
                );

        LeaderboardScoreEntity existing =
                new LeaderboardScoreEntity(
                        "dota",
                        "fedor",
                        500,
                        oldTime
                );

        when(leaderboardScoreRepository.findById(id))
                .thenReturn(Optional.of(existing));

        rankingService.actualiseLeaderboard(
                "dota",
                "fedor",
                100,
                requestTime
        );

        assertThat(existing.getScore())
                .isEqualTo(600);

        assertThat(existing.getUpdatedAt())
                .isEqualTo(requestTime);

        verify(leaderboardScoreRepository)
                .save(existing);
    }

    @Test
    void shouldCreateScoreForNewUser() {
        Instant requestTime =
                Instant.parse("2026-08-27T10:00:00Z");

        LeaderboardScoreId id =
                new LeaderboardScoreId(
                        "dota",
                        "fedor"
                );

        when(leaderboardScoreRepository.findById(id))
                .thenReturn(Optional.empty());

        rankingService.actualiseLeaderboard(
                "dota",
                "fedor",
                100,
                requestTime
        );

        ArgumentCaptor<LeaderboardScoreEntity> captor =
                ArgumentCaptor.forClass(
                        LeaderboardScoreEntity.class
                );

        verify(leaderboardScoreRepository)
                .save(captor.capture());

        LeaderboardScoreEntity saved =
                captor.getValue();

        assertThat(saved.getLeaderboardId())
                .isEqualTo("dota");

        assertThat(saved.getUserId())
                .isEqualTo("fedor");

        assertThat(saved.getScore())
                .isEqualTo(100);

        assertThat(saved.getUpdatedAt())
                .isEqualTo(requestTime);
    }
}