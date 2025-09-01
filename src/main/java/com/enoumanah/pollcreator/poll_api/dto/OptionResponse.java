package com.enoumanah.pollcreator.poll_api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OptionResponse {

    private Long id;
    private String text;
    private Long votes;

}
