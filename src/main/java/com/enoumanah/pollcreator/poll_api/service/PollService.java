package com.enoumanah.pollcreator.poll_api.service;

import com.enoumanah.pollcreator.poll_api.dto.*;
import com.enoumanah.pollcreator.poll_api.exception.InvalidVoteException;
import com.enoumanah.pollcreator.poll_api.exception.OptionNotFoundException;
import com.enoumanah.pollcreator.poll_api.exception.PollNotFoundException;
import com.enoumanah.pollcreator.poll_api.model.Option;
import com.enoumanah.pollcreator.poll_api.model.Poll;
import com.enoumanah.pollcreator.poll_api.model.User;
import com.enoumanah.pollcreator.poll_api.model.Vote;
import com.enoumanah.pollcreator.poll_api.repository.OptionRepository;
import com.enoumanah.pollcreator.poll_api.repository.PollRepository;
import com.enoumanah.pollcreator.poll_api.repository.UserRepository;
import com.enoumanah.pollcreator.poll_api.repository.VoteRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PollService {

    private final PollRepository pollRepository;
    private final OptionRepository optionRepository;
    private final VoteRepository voteRepository;
    private final UserRepository userRepository; // It's good practice to have this for user lookups

    public PollService(PollRepository pollRepository, OptionRepository optionRepository, VoteRepository voteRepository, UserRepository userRepository) {
        this.pollRepository = pollRepository;
        this.optionRepository = optionRepository;
        this.voteRepository = voteRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public PollResponse createPoll(CreatePollRequest request, Authentication authentication) {
        // CORRECT WAY TO GET USERNAME
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);

        Poll poll = new Poll();
        poll.setQuestion(request.getQuestion());
        poll.setVisibility(request.getVisibility() != null ? request.getVisibility() : "public");
        poll.setOwnerId(user.getId()); // Store the user's actual ID
        poll.setOwnerUsername(username); // Store the username
        poll.generateShareLinkIfPrivate();

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

        optionRepository.saveAll(options);

        poll.setOptions(options);
        pollRepository.save(poll);

        return mapToPollResponse(poll);
    }

    @Transactional
    public void voteOnOption(String pollId, VoteRequest request, Authentication authentication) {
        // CORRECT WAY TO GET USERNAME
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);

        Poll poll = pollRepository.findById(pollId)
                .orElseThrow(() -> new PollNotFoundException("Poll not found with ID: " + pollId));

        Option option = optionRepository.findById(request.getOptionId())
                .orElseThrow(() -> new OptionNotFoundException("Option not found with ID: " + request.getOptionId()));

        if (!option.getPollId().equals(pollId)) {
            throw new InvalidVoteException("Option does not belong to this poll");
        }

        if (voteRepository.findByPollIdAndUserId(pollId, user.getId()) != null) {
            throw new InvalidVoteException("User has already voted on this poll");
        }

        option.setVotes(option.getVotes() + 1);
        optionRepository.save(option);

        Vote vote = new Vote();
        vote.setPollId(pollId);
        vote.setOptionId(option.getId());
        vote.setUserId(user.getId()); // Store the user's actual ID
        voteRepository.save(vote);
    }

    @Transactional
    public void deletePoll(String id, Authentication authentication) {
        // CORRECT WAY TO GET USERNAME
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);

        Poll poll = pollRepository.findById(id)
                .orElseThrow(() -> new PollNotFoundException("Poll not found with ID: " + id));

        if (!poll.getOwnerId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized to delete this poll");
        }

        pollRepository.delete(poll);
    }

    // --- Methods from previous steps (already correct) ---

    public PollResponse getPollById(String id, Principal principal) {
        Poll poll = pollRepository.findById(id)
                .orElseThrow(() -> new PollNotFoundException("Poll not found with ID: " + id));

        if ("private".equals(poll.getVisibility())) {
            if (principal == null || !poll.getOwnerUsername().equals(principal.getName())) {
                throw new PollNotFoundException("Private poll - unauthorized access");
            }
        }
        return mapToPollResponse(poll);
    }

    public List<PollResponse> getDashboardPolls(Principal principal) {
        List<Poll> publicPolls = pollRepository.findByVisibility("public");

        User user = userRepository.findByUsername(principal.getName());
        List<Poll> userPolls = pollRepository.findByOwnerId(user.getId());

        List<Poll> dashboardPolls = Stream.concat(publicPolls.stream(), userPolls.stream())
                .distinct()
                .collect(Collectors.toList());

        return dashboardPolls.stream()
                .map(this::mapToPollResponse)
                .collect(Collectors.toList());
    }

    public List<PollResponse> getUserActivityPolls(Principal principal) {
        User user = userRepository.findByUsername(principal.getName());

        List<Poll> createdPolls = pollRepository.findByOwnerId(user.getId());

        List<Vote> userVotes = voteRepository.findAllByUserId(user.getId());
        List<String> votedPollIds = userVotes.stream()
                .map(Vote::getPollId)
                .collect(Collectors.toList());
        List<Poll> votedOnPolls = pollRepository.findAllById(votedPollIds);

        List<Poll> userActivityPolls = Stream.concat(createdPolls.stream(), votedOnPolls.stream())
                .distinct()
                .collect(Collectors.toList());

        return userActivityPolls.stream()
                .map(this::mapToPollResponse)
                .collect(Collectors.toList());
    }

    private PollResponse mapToPollResponse(Poll poll) {
        PollResponse response = new PollResponse();
        response.setId(poll.getId());
        response.setCreatedAt(poll.getCreatedAt());
        response.setQuestion(poll.getQuestion());
        response.setOwnerUsername(poll.getOwnerUsername());

        if (poll.getOptions() != null) {
            response.setOptions(poll.getOptions().stream()
                    .map(opt -> {
                        OptionResponse optRes = new OptionResponse();
                        optRes.setId(opt.getId());
                        optRes.setText(opt.getText());
                        optRes.setVotes(opt.getVotes());
                        return optRes;
                    })
                    .collect(Collectors.toList()));
        } else {
            response.setOptions(new ArrayList<>());
        }
        return response;
    }
}