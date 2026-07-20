package com.brainz.sms_backend.Student;

import com.brainz.sms_backend.Classroom.ClassRoom;
import com.brainz.sms_backend.Guardian.Guardian;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private int age;

    @ManyToOne
    private Guardian guardian;

    @ManyToOne
    private ClassRoom currentClass;
}