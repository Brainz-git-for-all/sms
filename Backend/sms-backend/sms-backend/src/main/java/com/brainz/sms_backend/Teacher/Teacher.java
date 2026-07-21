package com.brainz.sms_backend.Teacher;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String qualification;  // e.g., "BSc Mathematics"
    private String specialization; // e.g., "Mathematics"
}
