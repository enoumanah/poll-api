package com.enoumanah.pollcreator.poll_api.service;

import com.enoumanah.pollcreator.poll_api.dto.*;
import com.enoumanah.pollcreator.poll_api.exception.PollNotFoundException;
import com.enoumanah.pollcreator.poll_api.exception.InvalidVoteException;
import com.enoumanah.pollcreator.poll_api.exception.OptionNotFoundException;
import com.enoumanah.pollcreator.poll_api.model.Option;
import com.enoumanah.pollcreator.poll_api.model.Poll;
import com.enoumanah.pollcreator.poll_api.model.Vote;
import com.enoumanah.pollcreator.poll_api.repository.OptionRepository;
import com.enoumanah.pollcreator.poll_api.repository.PollRepository;
import com.enoumanah.pollcreator.poll_api.repository.VoteRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PollService {

    private final PollRepository pollRepository;
    private final OptionRepository optionRepository;
    private final VoteRepository voteRepository;

    public PollService(PollRepository pollRepository, OptionRepository optionRepository, VoteRepository voteRepository) {
        this.pollRepository = pollRepository;
        this.optionRepository = optionRepository;
        this.voteRepository = voteRepository;
    }

    @Transactional
    public PollResponse createPoll(CreatePollRequest request, Authentication authentication) {
        String ownerId = authentication.getPrincipal().toString();

        Poll poll = new Poll();
        poll.setQuestion(request.getQuestion());
        poll.setVisibility(request.getVisibility() != null ? request.getVisibility() : "public");
        poll.setOwnerId(ownerId);
        poll.generateShareLinkIfPrivate();

        // Save the poll first to generate its ID
        poll = pollRepository.save(poll);

        Poll finalPoll = poll;
        List<Option> options = request.getOptions().stream()
                .map(optText -> {
                    Option option = new Option();
                    option.setText(optText);
                    option.setPollId(finalPoll.getId());
                    return option;
                })
                .collect(Collectors.toList());

        // Save the options to generate their IDs
        optionRepository.saveAll(options);

        // Now, set the options on the poll and save it again to create the references
        poll.setOptions(options);
        pollRepository.save(poll);

        return mapToPollResponse(poll);
    }


    @Transactional
    public void voteOnOption(String pollId, VoteRequest request, Authentication authentication) {
        String userId = authentication.getPrincipal().toString();

        Poll poll = pollRepository.findById(pollId)
                .orElseThrow(() -> new PollNotFoundException("Poll not found with ID: " + pollId));

        Option option = optionRepository.findById(request.getOptionId())
                .orElseThrow(() -> new OptionNotFoundException("Option not found with ID: " + request.getOptionId()));

        if (!option.getPollId().equals(pollId)) {
            throw new InvalidVoteException("Option does not belong to this poll");
        }

        if (voteRepository.findByPollIdAndUserId(pollId, userId) != null) {
            throw new InvalidVoteException("User has already voted on this poll");
        }

        option.setVotes(option.getVotes() + 1);
        optionRepository.save(option);

        Vote vote = new Vote();
        vote.setPollId(pollId);
        vote.setOptionId(option.getId());
        vote.setUserId(userId);
        voteRepository.save(vote);
    }

    public PollResponse getPollById(String id, Principal principal) {
        Poll poll = pollRepository.findById(id)
                .orElseThrow(() -> new PollNotFoundException("Poll not found with ID: " + id));

        if ("private".equals(poll.getVisibility())) {
            if (principal == null || !poll.getOwnerId().equals(principal.getName())) {
                throw new PollNotFoundException("Private poll - unauthorized access");
            }
        }

        return mapToPollResponse(poll);
    }

    public List<PollResponse> getAllPublicPolls() {
        return pollRepository.findByVisibility("public").stream()
                .map(this::mapToPollResponse)
                .collect(Collectors.toList());
    }

    // THIS IS THE MISSING METHOD
    public PollResponse getPollByShareLink(String shareLink) {
        Poll poll = pollRepository.findByShareLink(shareLink);
        if (poll == null) {
            throw new PollNotFoundException("Poll not found with share link: " + shareLink);
        }
        return mapToPollResponse(poll);
    }

    @Transactional
    public void deletePoll(String id, Authentication authentication) {
        String userId = authentication.getPrincipal().toString();

        Poll poll = pollRepository.findById(id)
                .orElseThrow(() -> new PollNotFoundException("Poll not found with ID: " + id));

        if (!poll.getOwnerId().equals(userId)) {
            throw new RuntimeException("Unauthorized to delete this poll");
        }

        pollRepository.delete(poll);
    }

    public PollResultsResponse getPollResults(String id) {
        Poll poll = pollRepository.findById(id)
                .orElseThrow(() -> new PollNotFoundException("Poll not found with ID: " + id));

        long totalVotes = poll.getOptions().stream().mapToLong(Option::getVotes).sum();

        PollResultsResponse response = new PollResultsResponse();
        response.setQuestion(poll.getQuestion());
        response.setOptions(poll.getOptions().stream()
                .map(opt -> {
                    PollResultsResponse.OptionResult res = new PollResultsResponse.OptionResult();
                    res.setText(opt.getText());
                    res.setVotes(opt.getVotes());
                    res.setPercentage(totalVotes > 0 ? (opt.getVotes() * 100.0 / totalVotes) : 0.0);
                    return res;
                })
                .collect(Collectors.toList()));
        return response;
    }

    private PollResponse mapToPollResponse(Poll poll) {
        PollResponse response = new PollResponse();
        response.setId(poll.getId());
        response.setCreatedAt(poll.getCreatedAt());
        response.setQuestion(poll.getQuestion());
        response.setOptions(poll.getOptions().stream()
                .map(opt -> {
                    OptionResponse optRes = new OptionResponse();
                    optRes.setId(opt.getId());
                    optRes.setText(opt.getText());
                    optRes.setVotes(opt.getVotes());
                    return optRes;
                })
                .collect(Collectors.toList()));
        return response;
    }
}