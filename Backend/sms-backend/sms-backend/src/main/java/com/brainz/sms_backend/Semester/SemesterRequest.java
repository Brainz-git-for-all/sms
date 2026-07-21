package com.brainz.sms_backend.Semester;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class SemesterRequest {
    @NotBlank(message = "Term name is required")
    private String termName;
    @NotBlank(message = "Academic year is required")
    private String academicYear;
    @NotNull(message = "Start date is required")
    private LocalDate startDate;
    @NotNull(message = "End date is required")
    private LocalDate endDate;
}
