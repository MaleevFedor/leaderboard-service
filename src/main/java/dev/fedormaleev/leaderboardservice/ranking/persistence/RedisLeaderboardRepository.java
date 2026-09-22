package dev.fedormaleev.leaderboardservice.ranking.persistence;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public class RedisLeaderboardRepository {

    private static final String KEY_PREFIX = "leaderboard:";

    private final StringRedisTemplate redisTemplate;

    public RedisLeaderboardRepository(
            StringRedisTemplate redisTemplate
    ) {
        this.redisTemplate = redisTemplate;
    }

    private String getKey(String leaderboardId) {
        return KEY_PREFIX + leaderboardId + ":scores";
    }

    public Double incrementScore(
            String leaderboardId,
            String userId,
            long points
    ) {
        return redisTemplate
                .opsForZSet()
                .incrementScore(
                        getKey(leaderboardId),
                        userId,
                        points
                );
    }

    public Set<ZSetOperations.TypedTuple<String>> getTop(
            String leaderboardId,
            long limit
    ) {
        return redisTemplate
                .opsForZSet()
                .reverseRangeWithScores(
                        getKey(leaderboardId),
                        0,
                        limit - 1
                );
    }

    public Long getRank(
            String leaderboardId,
            String userId
    ) {
        return redisTemplate
                .opsForZSet()
                .reverseRank(
                        getKey(leaderboardId),
                        userId
                );
    }
}