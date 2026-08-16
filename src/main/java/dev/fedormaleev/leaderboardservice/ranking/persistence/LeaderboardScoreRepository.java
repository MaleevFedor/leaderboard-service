package dev.fedormaleev.leaderboardservice.ranking.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaderboardScoreRepository
        extends JpaRepository<LeaderboardScoreEntity, LeaderboardScoreId> {
}