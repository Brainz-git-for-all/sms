package com.brainz.sms_backend.Subject;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SubjectRequest {
    @NotBlank(message = "Subject name is required")
    private String name;
    @NotBlank(message = "Subject code is required")
    private String code;
    private String description;
    private Long teacherId;
}
