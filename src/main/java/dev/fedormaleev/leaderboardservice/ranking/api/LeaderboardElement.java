package dev.fedormaleev.leaderboardservice.ranking.api;

import java.time.Instant;

public record LeaderboardElement(
        String userId,
        long score,
        Instant updatedAt
) {
}