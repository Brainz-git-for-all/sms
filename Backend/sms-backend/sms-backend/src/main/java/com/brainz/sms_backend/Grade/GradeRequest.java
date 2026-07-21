package com.brainz.sms_backend.Grade;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class GradeRequest {
    @NotNull(message = "Student ID is required")
    private Long studentId;
    @NotNull(message = "Subject ID is required")
    private Long subjectId;
    @NotNull(message = "Semester ID is required")
    private Long semesterId;
    @NotNull(message = "Test 1 score is required") @Min(0) @Max(20)
    private Double test1;
    @NotNull(message = "Test 2 score is required") @Min(0) @Max(20)
    private Double test2;
    @NotNull(message = "Exam score is required") @Min(0) @Max(60)
    private Double exams;
}
