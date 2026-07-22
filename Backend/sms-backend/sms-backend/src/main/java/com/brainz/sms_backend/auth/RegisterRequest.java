package com.brainz.sms_backend.auth;

// YOUR TASK — File 3
// Annotations: @Data, @NoArgsConstructor, @AllArgsConstructor
// Fields:
//   String username  — @NotBlank, @Size(min=3, max=50)
//   String password  — @NotBlank, @Size(min=6)
//   Role   role      — @NotNull  (uses your Role enum)

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    // TODO: add annotations and fields
    private String username;
    private String password;
    private Role role;
}
