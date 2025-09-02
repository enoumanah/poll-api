package com.enoumanah.pollcreator.poll_api.service;


import com.enoumanah.pollcreator.poll_api.dto.*;
import com.enoumanah.pollcreator.poll_api.exception.InvalidVoteException;
import com.enoumanah.pollcreator.poll_api.exception.OptionNotFoundException;
import com.enoumanah.pollcreator.poll_api.exception.PollNotFoundException;
import com.enoumanah.pollcreator.poll_api.model.Option;
import com.enoumanah.pollcreator.poll_api.model.Poll;
import com.enoumanah.pollcreator.poll_api.repository.OptionRepository;
import com.enoumanah.pollcreator.poll_api.repository.PollRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PollService {

    private final PollRepository pollRepository;
    private final OptionRepository optionRepository;

    public PollService(PollRepository pollRepository, OptionRepository optionRepository) {
        this.pollRepository = pollRepository;
        this.optionRepository = optionRepository;
    }


    @Transactional
    public PollResponse createPoll(CreatePollRequest request){
        Poll poll = new Poll();
        poll.setQuestion(request.getQuestion());

        poll = pollRepository.save(poll);

        Poll finalPoll = poll;
        List<Option> options = request.getOptions().stream()
                .map(optText -> {
                    Option option = new Option();
                    option.setText(optText);
                    option.setVotes(0L);
                    option.setPoll(finalPoll);
                    return option;
                }).collect(Collectors.toList());

        options = optionRepository.saveAll(options);

        poll.setOptions(options);
        return mapToPollResponse(poll);
    }

    @Transactional
    public PollResultsResponse voteOnOption(Long pollId, VoteRequest voteRequest){
        Poll poll = pollRepository.findById(pollId)
                .orElseThrow(() -> new PollNotFoundException("Poll not found with Id: " + pollId));
        Option option = optionRepository.findById(voteRequest.getOptionId())
                .orElseThrow(() -> new OptionNotFoundException("Options not found with Id: " + voteRequest.getOptionId()));

        if (!option.getPoll().getId().equals(pollId)){
            throw new InvalidVoteException("Option does not belong to this poll");
        }

        option.setVotes(option.getVotes() + 1);
        optionRepository.save(option);

        return getPollResults(pollId);
    }

    public void deletePoll(Long id){
        Poll poll = pollRepository.findById(id)
                .orElseThrow(() -> new PollNotFoundException("Poll not found with Id: " +id));
        poll.clearOptions();
        pollRepository.delete(poll);
    }

    public PollResponse getPollById(Long id){
        Poll poll = pollRepository.findById(id)
                .orElseThrow(()-> new PollNotFoundException("Poll not found with Id: " +id));
        return mapToPollResponse(poll);
    }

    public Page<PollResponse> getAllPolls(Pageable pageable) {
        Page<Poll> polls = pollRepository.findAll(pageable);
        return polls.map(this::mapToPollResponse);
    }

    public PollResultsResponse getPollResults(Long id){
        Poll poll = pollRepository.findById(id)
                .orElseThrow(()-> new PollNotFoundException("Poll not found with Id: " +id));

        long totalVotes = poll.getOptions().stream().mapToLong(Option::getVotes).sum();

        PollResultsResponse response = new PollResultsResponse();
        response.setQuestion(poll.getQuestion());
        response.setOptions(poll.getOptions().stream()
                .map(option -> {
                    PollResultsResponse.OptionResult result = new PollResultsResponse.OptionResult();
                    result.setText(option.getText());
                    result.setVotes(option.getVotes());
                    result.setPercentage(totalVotes > 0 ? (option.getVotes() * 100 / totalVotes) : 0.0);
                    return result;
                }).collect(Collectors.toList()));

        return response;
    }

    private PollResponse mapToPollResponse(Poll poll){
        PollResponse response = new PollResponse();
        response.setId(poll.getId());
        response.setCreatedAt(poll.getCreatedAt());
        response.setQuestion(poll.getQuestion());
        response.setOptions(poll.getOptions().stream()
                .map(option -> {
                    OptionResponse optionResponse = new OptionResponse();
                    optionResponse.setId(option.getId());
                    optionResponse.setText(option.getText());
                    optionResponse.setVotes(option.getVotes());
                    return optionResponse;
                }).collect(Collectors.toList()));

        return response;
    }

}
