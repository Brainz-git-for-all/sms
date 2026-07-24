package com.brainz.sms_backend.Student;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class StudentRequest {
    @NotBlank(message = "First name is required")
    private String firstName;
    @NotBlank(message = "Last name is required")
    private String lastName;
    @Email(message = "Invalid email")
    private String email;
    private String phone;
    @NotNull(message = "Gender is required")
    private Gender gender;
    @NotNull(message = "Date of birth is required")
    private LocalDate dateOfBirth;
    private LocalDate enrollmentDate;
    private StudentStatus status;
    private Long guardianId;
    private Long currentClassId;
}
