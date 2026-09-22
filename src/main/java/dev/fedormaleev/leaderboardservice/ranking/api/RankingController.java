package dev.fedormaleev.leaderboardservice.ranking.api;

import dev.fedormaleev.leaderboardservice.ranking.api.dto.LeaderboardResponse;
import dev.fedormaleev.leaderboardservice.ranking.api.dto.RankResponse;
import dev.fedormaleev.leaderboardservice.ranking.service.RankingService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api/leaderboard/{leaderboardId}")
public class RankingController {
    private final RankingService rankingService;

    public RankingController(RankingService rankingService){ this.rankingService = rankingService; }

    @GetMapping("/top")
    @ResponseStatus(HttpStatus.OK)
    public LeaderboardResponse getEvents(
            @PathVariable String leaderboardId,
            @RequestParam(defaultValue = "5") int limit
    ){
        return rankingService.getTop(leaderboardId, limit);
    }

    @GetMapping("/user/{userId}/get-rank")
    @ResponseStatus(HttpStatus.OK)
    public RankResponse getRank(
            @PathVariable String leaderboardId,
            @PathVariable String userId
    ){
        return new RankResponse(leaderboardId, userId,
                rankingService.getRank(leaderboardId, userId), Instant.now());
    }
}
