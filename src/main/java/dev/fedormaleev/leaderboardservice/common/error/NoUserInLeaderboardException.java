package dev.fedormaleev.leaderboardservice.common.error;

public class NoUserInLeaderboardException extends RuntimeException{
    public NoUserInLeaderboardException(
            String leaderboardId,
            String userId
    ) {
        super("User " + userId + " not found in leaderboard " + leaderboardId);
    }
}
