package com.enoumanah.pollcreator.poll_api.controller;

import com.enoumanah.pollcreator.poll_api.dto.CreatePollRequest;
import com.enoumanah.pollcreator.poll_api.dto.PollResponse;
import com.enoumanah.pollcreator.poll_api.dto.PollResultsResponse;
import com.enoumanah.pollcreator.poll_api.dto.VoteRequest;
import com.enoumanah.pollcreator.poll_api.service.PollService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")  // Restrict to specific origins in production, e.g., "http://localhost:3000"
@RestController
@RequestMapping("/api/polls")
public class PollController {

    private final PollService pollService;

    public PollController(PollService pollService) {
        this.pollService = pollService;
    }

    @PostMapping
    public ResponseEntity<PollResponse> createPoll(@Valid @RequestBody CreatePollRequest pollRequest) {
        return new ResponseEntity<>(pollService.createPoll(pollRequest), HttpStatus.CREATED);
    }

    @PostMapping("/{id}/votes")
    public ResponseEntity<PollResultsResponse> voteOnOption(
            @PathVariable Long id,
            @Valid @RequestBody VoteRequest request) {
        pollService.voteOnOption(id, request);
        return ResponseEntity.ok(pollService.getPollResults(id));
    }

    @GetMapping
    public ResponseEntity<Page<PollResponse>> getAllPolls(Pageable pageable) {
        return ResponseEntity.ok(pollService.getAllPolls(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PollResponse> getPollById(@PathVariable Long id) {
        return ResponseEntity.ok(pollService.getPollById(id));
    }

    @GetMapping("/{id}/results")
    public ResponseEntity<PollResultsResponse> getPollResults(@PathVariable Long id) {
        return ResponseEntity.ok(pollService.getPollResults(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePoll(@PathVariable Long id){
        pollService.deletePoll(id);
        return ResponseEntity.noContent().build();
    }
}