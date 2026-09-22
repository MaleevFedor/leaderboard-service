package dev.fedormaleev.leaderboardservice.score.api.dto;

import java.time.Instant;

public record AddScoreResponse(
        String leaderboardId,
        String userId,
        long points,
        Instant occurredAt,
        Instant createdAt
) {}