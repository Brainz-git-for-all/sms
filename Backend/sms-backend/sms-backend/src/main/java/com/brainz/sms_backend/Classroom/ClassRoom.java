package com.brainz.sms_backend.Classroom;

import com.brainz.sms_backend.Teacher.Teacher;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String room;
    private String academicYear;
    private Integer capacity;

    @Enumerated(EnumType.STRING)
    private ClassRoomStatus status;

    @Enumerated(EnumType.STRING)
    private ActiveStatus active;

    @ManyToOne
    private Teacher classTeacher;
}
