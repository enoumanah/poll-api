package com.enoumanah.pollcreator.poll_api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
public class PollResponse {

    private String id;
    private Instant createdAt;
    private String question;
    private List<OptionResponse> options;
    private String ownerUsername;
    private String visibility;
    private String shareLink;

}
