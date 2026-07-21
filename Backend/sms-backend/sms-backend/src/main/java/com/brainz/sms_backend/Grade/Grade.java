package com.brainz.sms_backend.Grade;

import com.brainz.sms_backend.Semester.Semester;
import com.brainz.sms_backend.Student.Student;
import com.brainz.sms_backend.Subject.Subject;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double test1;       // max 20
    private Double test2;       // max 20
    private Double exams;       // max 60
    private Double total;       // auto-calculated: test1 + test2 + exams
    private String gradeLetter; // A, B, C, D, F — auto-assigned
    private String remarks;     // DISTINCTION, PASS, FAIL — auto-assigned

    @ManyToOne
    private Student student;

    @ManyToOne
    private Subject subject;

    @ManyToOne
    private Semester semester;
}
