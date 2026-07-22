package com.brainz.sms_backend.auth;

// YOUR TASK — File 2
// Annotations: @Data, @NoArgsConstructor, @AllArgsConstructor (Lombok)
// Fields:
//   String username  — @NotBlank
//   String password  — @NotBlank

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {
    // TODO: add Lombok annotations on the class
    // TODO: add fields with validation annotations
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
