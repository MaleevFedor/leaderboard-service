package dev.fedormaleev.leaderboardservice.ranking.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "leaderboard_scores")
@IdClass(LeaderboardScoreId.class)
public class LeaderboardScoreEntity {

    @Id
    @Column(name = "leaderboard_id", nullable = false, length = 64)
    private String leaderboardId;

    @Id
    @Column(name = "user_id", nullable = false, length = 64)
    private String userId;

    @Column(nullable = false)
    private long score;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected LeaderboardScoreEntity() {
        // JPA
    }

    public LeaderboardScoreEntity(
            String leaderboardId,
            String userId,
            long score,
            Instant updatedAt
    ) {
        this.leaderboardId = leaderboardId;
        this.userId = userId;
        this.score = score;
        this.updatedAt = updatedAt;
    }

    public String getLeaderboardId() {
        return leaderboardId;
    }

    public String getUserId() {
        return userId;
    }

    public long getScore() {
        return score;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
