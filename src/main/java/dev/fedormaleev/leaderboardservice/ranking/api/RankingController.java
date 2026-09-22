package dev.fedormaleev.leaderboardservice.ranking.api;

import dev.fedormaleev.leaderboardservice.ranking.service.RankingService;
import dev.fedormaleev.leaderboardservice.score.api.GetScoreEventsResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
}
