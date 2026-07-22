package com.brainz.sms_backend.auth;

import lombok.*;

// YOUR TASK — File 4
// Annotations: @Data, @AllArgsConstructor, @NoArgsConstructor, @Builder
// Fields:
//   String token     — the JWT token to send back to frontend
//   String username  — the logged-in username
//   String role      — the role as a string, e.g. "ADMIN"
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class AuthResponse {
    // TODO: add annotations and fields
    private String token;
    private String username;
    private String role;
}
