package com.brainz.sms_backend.Student;

import com.brainz.sms_backend.Classroom.ClassRoom;
import com.brainz.sms_backend.Guardian.Guardian;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDate dateOfBirth;
    private LocalDate enrollmentDate;

    @Enumerated(EnumType.STRING)
    private StudentStatus status;

    @ManyToOne
    private Guardian guardian;

    @ManyToOne
    private ClassRoom currentClass;
}
