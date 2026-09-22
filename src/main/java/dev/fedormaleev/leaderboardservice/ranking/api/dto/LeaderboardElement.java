package dev.fedormaleev.leaderboardservice.ranking.api.dto;

import java.time.Instant;

public record LeaderboardElement(
        String userId,
        long score,
        int rank,
        Instant updatedAt
) {
}