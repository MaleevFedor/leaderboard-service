CREATE TABLE score_events
(
    id             UUID PRIMARY KEY,
    leaderboard_id VARCHAR(64)  NOT NULL,
    user_id        VARCHAR(64)  NOT NULL,
    points         BIGINT       NOT NULL,
    occurred_at    TIMESTAMPTZ  NOT NULL,
    created_at     TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT score_events_points_not_zero
        CHECK (points <> 0)
);

CREATE INDEX idx_score_events_leaderboard_user_history
    ON score_events (
                     leaderboard_id,
                     user_id,
                     occurred_at DESC
        );

CREATE INDEX idx_score_events_leaderboard_rebuild
    ON score_events (
                     leaderboard_id,
                     user_id
        );
