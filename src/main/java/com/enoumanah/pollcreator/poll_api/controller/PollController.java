package com.enoumanah.pollcreator.poll_api.controller;

import com.enoumanah.pollcreator.poll_api.dto.CreatePollRequest;
import com.enoumanah.pollcreator.poll_api.dto.PollResponse;
import com.enoumanah.pollcreator.poll_api.dto.PollResultsResponse;
import com.enoumanah.pollcreator.poll_api.dto.VoteRequest;
import com.enoumanah.pollcreator.poll_api.service.PollService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*") // Update to Vercel URL in prod
@RestController
@RequestMapping("/api/polls")
public class PollController {

    private final PollService pollService;

    public PollController(PollService pollService) {
        this.pollService = pollService;
    }

    @PostMapping
    public ResponseEntity<PollResponse> createPoll(@Valid @RequestBody CreatePollRequest request, Authentication authentication) {
        return new ResponseEntity<>(pollService.createPoll(request, authentication), HttpStatus.CREATED);
    }

    @PostMapping("/{id}/vote")
    public ResponseEntity<Void> voteOnOption(@PathVariable String id, @Valid @RequestBody VoteRequest request, Authentication authentication) {
        pollService.voteOnOption(id, request, authentication);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PollResponse> getPollById(@PathVariable String id, Authentication authentication) {
        return ResponseEntity.ok(pollService.getPollById(id, authentication));
    }

    @GetMapping
    public ResponseEntity<List<PollResponse>> getAllPolls(Authentication authentication) {
        return ResponseEntity.ok(pollService.getAllPolls(authentication));
    }

    @GetMapping("/share/{shareLink}")
    public ResponseEntity<PollResponse> getPollByShareLink(@PathVariable String shareLink) {
        return ResponseEntity.ok(pollService.getPollByShareLink(shareLink));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePoll(@PathVariable String id, Authentication authentication) {
        pollService.deletePoll(id, authentication);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/results")
    public ResponseEntity<PollResultsResponse> getPollResults(@PathVariable String id) {
        // Implement results calculation as before
        return ResponseEntity.ok(pollService.getPollResults(id));
    }
}