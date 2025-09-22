package com.enoumanah.pollcreator.poll_api.repository;

import com.enoumanah.pollcreator.poll_api.model.Poll;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PollRepository extends MongoRepository<Poll, String> {
    List<Poll> findByVisibility(String visibility);
    Poll findByShareLink(String shareLink);
}