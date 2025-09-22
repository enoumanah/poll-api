package com.enoumanah.pollcreator.poll_api.repository;

import com.enoumanah.pollcreator.poll_api.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    User findByUsername(String username);
}
