package dev.fedormaleev.leaderboardservice.score.api;

import dev.fedormaleev.leaderboardservice.score.service.ScoreService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/leaderboard/{leaderboardId}")
public class ScoreController {
    private final ScoreService scoreService;

    public ScoreController(ScoreService scoreService){
        this.scoreService = scoreService;
    }

    @PostMapping("/add-score")
    @ResponseStatus(HttpStatus.CREATED)
    public AddScoreResponse addScore(
            @PathVariable String leaderboardId,
            @Valid @RequestBody AddScoreRequest request){
        return scoreService.addScore(leaderboardId, request);
    }

    @GetMapping("/user/{userId}/get-events")
    @ResponseStatus(HttpStatus.OK)
    public GetScoreEventsResponse getEvents(
            @PathVariable String leaderboardId,
            @PathVariable String userId){
        return scoreService.getEvents(leaderboardId, userId);
    }
}
