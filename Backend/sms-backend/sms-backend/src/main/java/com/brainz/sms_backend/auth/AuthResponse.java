package com.brainz.sms_backend.auth;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class AuthResponse {

    private String token;
    private String username;
    private String role;
}
