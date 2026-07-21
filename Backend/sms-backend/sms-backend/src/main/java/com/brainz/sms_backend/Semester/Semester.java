package com.brainz.sms_backend.Semester;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Semester {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String termName;     // e.g., "First Term"
    private String academicYear; // e.g., "2024/2025"
    private LocalDate startDate;
    private LocalDate endDate;
}
