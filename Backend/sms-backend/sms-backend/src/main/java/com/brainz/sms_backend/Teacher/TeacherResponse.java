package com.brainz.sms_backend.Teacher;

import lombok.Data;

@Data
public class TeacherResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String qualification;
    private String specialization;

    public static TeacherResponse from(Teacher t) {
        TeacherResponse r = new TeacherResponse();
        r.setId(t.getId());
        r.setFirstName(t.getFirstName());
        r.setLastName(t.getLastName());
        r.setEmail(t.getEmail());
        r.setPhone(t.getPhone());
        r.setQualification(t.getQualification());
        r.setSpecialization(t.getSpecialization());
        return r;
    }
}
