package com.enoumanah.pollcreator.poll_api.repository;

import com.enoumanah.pollcreator.poll_api.model.Vote;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VoteRepository extends MongoRepository<Vote, String> {
    Vote findByPollIdAndUserId(String pollId, String userId);
}