package dev.fedormaleev.leaderboardservice.score.persistence;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ScoreEventRepository
        extends JpaRepository<ScoreEventEntity, UUID> {
}