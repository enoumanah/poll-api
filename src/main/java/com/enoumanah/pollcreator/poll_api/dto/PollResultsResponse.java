package com.enoumanah.pollcreator.poll_api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PollResultsResponse {

    private String question;
    private List<OptionResult> options;

    @Data
    @NoArgsConstructor
    public static class OptionResult {
        private String id;
        private String text;
        private Long votes;
        private Double percentage;
    }

}
