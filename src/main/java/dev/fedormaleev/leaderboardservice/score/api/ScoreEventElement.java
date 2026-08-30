package dev.fedormaleev.leaderboardservice.score.api;

import java.time.Instant;

public record ScoreEventElement (
        long points,
        Instant occurredAt,
        Instant createdAt
){}
