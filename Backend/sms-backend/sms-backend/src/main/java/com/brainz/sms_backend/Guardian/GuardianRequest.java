package com.brainz.sms_backend.Guardian;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GuardianRequest {
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Phone is required")
    private String phone;
    @Email(message = "Invalid email")
    private String email;
    private String address;
    @NotBlank(message = "Relationship is required")
    private String relationship;
    private String occupation;
}
