package com.brainz.sms_backend.Grade;

import com.brainz.sms_backend.Semester.Semester;
import com.brainz.sms_backend.Student.Student;
import com.brainz.sms_backend.Subject.Subject;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double test1;
    private Double test2;
    private Double exams;
    private Double total;

    @ManyToOne
    private Student student;

    @ManyToOne
    private Subject subject;

    @ManyToOne
    private Semester semester;
}