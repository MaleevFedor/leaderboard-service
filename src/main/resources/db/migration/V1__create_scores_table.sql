CREATE TABLE leaderboard_scores
(
    leaderboard_id  VARCHAR(64) NOT NULL,
    user_id         VARCHAR(64) NOT NULL,
    score           BIGINT NOT NULL DEFAULT 0,
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (leaderboard_id, user_id)
);
