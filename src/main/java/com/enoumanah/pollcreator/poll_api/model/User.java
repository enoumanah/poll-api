package com.enoumanah.pollcreator.poll_api.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Document(collection = "users")
@NoArgsConstructor
@Getter @Setter
public class User {

    @Id
    private String id;

    @Field("username")
    private String username;

    @Field("password")
    private String password;  // Hashed

    @Field("email")
    private String email;

    @Field("created_at")
    private Instant createdAt = Instant.now();
}