package com.enoumanah.pollcreator.poll_api.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef; // Import this
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Document(collection = "polls")
@NoArgsConstructor
@Getter @Setter
public class Poll {

    @Id
    private String id;

    @Field("question")
    private String question;

    @DBRef
    @Field("options")
    private List<Option> options;

    @Field("visibility")
    private String visibility = "public";

    @Field("share_link")
    private String shareLink;

    @Field("owner_id")
    private String ownerId;

    @Field("owner_username")
    private String ownerUsername;


    @Field("created_at")
    private Instant createdAt = Instant.now();

    public void generateShareLinkIfPrivate() {
        if ("private".equals(visibility)) {
            this.shareLink = UUID.randomUUID().toString();
        }
    }
}