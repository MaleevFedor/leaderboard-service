package dev.fedormaleev.leaderboardservice.score.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public record AddScoreRequest(
        @NotBlank
        @Size(max = 64)
        String userId,

        @NotNull
        long points,

        @NotNull
        Instant occurredAt
) {}
