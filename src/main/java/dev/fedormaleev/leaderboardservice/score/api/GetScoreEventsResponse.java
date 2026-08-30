package dev.fedormaleev.leaderboardservice.score.api;

import java.util.List;

public record GetScoreEventsResponse (
        String userId,
        String leaderboardId,
        List<ScoreEventElement> entries
) {}
