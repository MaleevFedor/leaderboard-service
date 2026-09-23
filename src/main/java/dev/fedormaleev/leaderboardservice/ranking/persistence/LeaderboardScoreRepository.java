package dev.fedormaleev.leaderboardservice.ranking.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeaderboardScoreRepository
        extends JpaRepository<LeaderboardScoreEntity, LeaderboardScoreId> {
    List<LeaderboardScoreEntity> findAllByLeaderboardId(String leaderboardId);
}