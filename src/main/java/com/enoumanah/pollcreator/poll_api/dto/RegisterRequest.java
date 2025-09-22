package com.enoumanah.pollcreator.poll_api.dto;

import lombok.*;

@Data
@NoArgsConstructor
public class RegisterRequest {

    private String username;
    private String password;
    private String email;

}
