package com.enoumanah.pollcreator.poll_api.dto;

import jakarta.validation.constraints.NotBlank; // Import @NotBlank
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VoteRequest {

    // The @NotNull is redundant since @NotBlank checks for null, empty, and whitespace-only strings.
    // We'll keep it simple with just @NotBlank.
    @NotBlank(message = "Option Id cannot be blank")
    private String optionId;

}