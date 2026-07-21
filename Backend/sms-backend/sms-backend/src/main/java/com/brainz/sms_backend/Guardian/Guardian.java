package com.brainz.sms_backend.Guardian;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Guardian {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String phone;
    private String email;
    private String address;
    private String relationship; // "Father", "Mother", "Uncle", etc.
    private String occupation;
}
