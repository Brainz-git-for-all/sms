package com.brainz.sms_backend.Subject;

import com.brainz.sms_backend.Teacher.Teacher;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String code;

    @ManyToOne
    private Teacher teacher; // Optional teacher
}

