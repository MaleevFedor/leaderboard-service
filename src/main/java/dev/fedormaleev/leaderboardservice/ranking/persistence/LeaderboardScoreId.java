package dev.fedormaleev.leaderboardservice.ranking.persistence;

import java.io.Serializable;
import java.util.Objects;

public class LeaderboardScoreId implements Serializable {

    private String leaderboardId;
    private String userId;

    public LeaderboardScoreId() {
    }

    public LeaderboardScoreId(String leaderboardId, String userId) {
        this.leaderboardId = leaderboardId;
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LeaderboardScoreId that)) return false;
        return Objects.equals(leaderboardId, that.leaderboardId)
                && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(leaderboardId, userId);
    }
}