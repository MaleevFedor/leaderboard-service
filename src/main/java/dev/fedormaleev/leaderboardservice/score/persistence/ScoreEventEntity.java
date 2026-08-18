package dev.fedormaleev.leaderboardservice.score.persistence;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;


@Entity
@Table(name = "score_events")
public class ScoreEventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "leaderboard_id", nullable = false, length = 64)
    private String leaderboardId;

    @Column(name = "user_id", nullable = false, length = 64)
    private String userId;

    @Column(name = "points", nullable = false)
    private long points;

    @Column(name = "occurred_at", nullable = false)
    private Instant occurredAt;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected ScoreEventEntity() {
    }

    public ScoreEventEntity(
            String leaderboardId,
            String userId,
            long points,
            Instant occurredAt,
            Instant createdAt
    ) {
        this.leaderboardId = leaderboardId;
        this.userId = userId;
        this.points = points;
        this.occurredAt = occurredAt;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public String getLeaderboardId() {
        return leaderboardId;
    }

    public String getUserId() {
        return userId;
    }

    public long getPoints() {
        return points;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
