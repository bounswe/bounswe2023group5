package com.app.gamereview.controller;

import com.app.gamereview.dto.request.vote.CreateVoteRequestDto;
import com.app.gamereview.dto.request.vote.GetAllVotesFilterRequestDto;
import com.app.gamereview.model.User;
import com.app.gamereview.model.Vote;
import com.app.gamereview.service.VoteService;
import com.app.gamereview.util.validation.annotation.AuthorizationRequired;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vote")
@Validated
public class VoteController {

    private final VoteService voteService;

    @Autowired
    public VoteController(
            VoteService voteService
    ) {
        this.voteService = voteService;
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<Vote>> getVotes(@ParameterObject GetAllVotesFilterRequestDto filter) {
        List<Vote> votes = voteService.getAllVotes(filter);
        return ResponseEntity.ok(votes);
    }

    @GetMapping("/get")
    public ResponseEntity<Vote> getVote(@RequestParam String id) {
        Vote vote = voteService.getVote(id);

        return ResponseEntity.ok(vote);
    }

    @AuthorizationRequired
    @PostMapping("/create")
    public ResponseEntity<Vote> createReview(@Valid @RequestBody CreateVoteRequestDto createVoteRequestDto,
                                               @RequestHeader String Authorization, HttpServletRequest request) {
        User user = (User) request.getAttribute("authenticatedUser");
        Vote voteToCreate = voteService.addVote(createVoteRequestDto, user);
        return ResponseEntity.ok(voteToCreate);
    }
}
