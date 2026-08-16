package dev.fedormaleev.leaderboardservice.score.api;

import java.time.Instant;
import java.util.UUID;

public record AddScoreResponse(
        String leaderboardId,
        String userId,
        long points,
        Instant occurredAt,
        Instant createdAt
) {}