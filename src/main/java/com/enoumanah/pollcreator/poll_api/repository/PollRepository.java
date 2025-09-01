package com.enoumanah.pollcreator.poll_api.repository;

import com.enoumanah.pollcreator.poll_api.model.Poll;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PollRepository extends JpaRepository<Poll, Long> {
}
