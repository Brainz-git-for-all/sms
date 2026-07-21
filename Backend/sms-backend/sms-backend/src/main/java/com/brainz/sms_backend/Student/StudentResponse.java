package com.brainz.sms_backend.Student;

import com.brainz.sms_backend.Classroom.ClassRoomResponse;
import com.brainz.sms_backend.Guardian.GuardianResponse;
import lombok.Data;
import java.time.LocalDate;

@Data
public class StudentResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String gender;
    private LocalDate dateOfBirth;
    private LocalDate enrollmentDate;
    private String status;
    private GuardianResponse guardian;
    private ClassRoomResponse currentClass;

    public static StudentResponse from(Student s) {
        StudentResponse r = new StudentResponse();
        r.setId(s.getId());
        r.setFirstName(s.getFirstName());
        r.setLastName(s.getLastName());
        r.setEmail(s.getEmail());
        r.setPhone(s.getPhone());
        r.setGender(s.getGender());
        r.setDateOfBirth(s.getDateOfBirth());
        r.setEnrollmentDate(s.getEnrollmentDate());
        r.setStatus(s.getStatus());
        if (s.getGuardian() != null)
            r.setGuardian(GuardianResponse.from(s.getGuardian()));
        if (s.getCurrentClass() != null)
            r.setCurrentClass(ClassRoomResponse.from(s.getCurrentClass()));
        return r;
    }
}
