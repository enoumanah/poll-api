package com.enoumanah.pollcreator.poll_api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VoteRequest {

    @NotNull(message = "Option Id cannot be null")
    @Min(value = 1, message = "Option Id must at least be 1")
    private String optionId;

}
