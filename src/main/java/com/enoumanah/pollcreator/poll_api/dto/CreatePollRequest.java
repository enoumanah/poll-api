package com.enoumanah.pollcreator.poll_api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CreatePollRequest {

    @NotBlank(message = "Question cannot be blank")
    private String question;

    @Size(min = 2, message = "At least 2 options are required")
    private List<@NotBlank(message = "Options cannot be blank") String> options;

}
