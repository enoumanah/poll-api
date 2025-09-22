package com.enoumanah.pollcreator.poll_api.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Document(collection = "votes")
@NoArgsConstructor
@Getter @Setter
public class Vote {

    @Id
    private String id;

    @Field("poll_id")
    private String pollId;

    @Field("option_id")
    private String optionId;

    @Field("user_id")
    private String userId;

    @Field("voted_at")
    private Instant votedAt = Instant.now();
}