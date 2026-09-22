package dev.fedormaleev.leaderboardservice.score.api.dto;

import java.time.Instant;

public record ScoreEventElement (
        long points,
        Instant occurredAt,
        Instant createdAt
){}
