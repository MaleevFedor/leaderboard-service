package dev.fedormaleev.leaderboardservice.ranking.api.dto;

import java.time.Instant;

public record RankResponse(
        String leaderboardId,
        String userId,
        long rank,
        Instant formedAt
) {}
