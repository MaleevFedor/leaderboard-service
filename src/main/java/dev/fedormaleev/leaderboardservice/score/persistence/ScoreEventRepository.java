package dev.fedormaleev.leaderboardservice.score.persistence;


import org.springframework.data.jpa.repository.JpaRepository;
import dev.fedormaleev.leaderboardservice.score.api.ScoreEventElement;

import java.util.List;
import java.util.UUID;

public interface ScoreEventRepository extends JpaRepository<ScoreEventEntity, UUID> {
    List<ScoreEventElement> findAllByLeaderboardIdAndUserId(
            String leaderboardId,
            String userId
    );
}