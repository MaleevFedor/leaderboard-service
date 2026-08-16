package dev.fedormaleev.leaderboardservice.ranking.api;


import java.util.List;

public record LeaderboardResponse(
        String leaderboardId,
        List<LeaderboardElement> entries
) {
}
