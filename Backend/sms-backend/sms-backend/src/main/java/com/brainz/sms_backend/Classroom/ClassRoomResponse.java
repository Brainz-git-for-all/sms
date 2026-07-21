package com.brainz.sms_backend.Classroom;

import com.brainz.sms_backend.Teacher.TeacherResponse;
import lombok.Data;

@Data
public class ClassRoomResponse {
    private Long id;
    private String name;
    private String room;
    private String academicYear;
    private Integer capacity;
    private TeacherResponse classTeacher;

    public static ClassRoomResponse from(ClassRoom c) {
        ClassRoomResponse r = new ClassRoomResponse();
        r.setId(c.getId());
        r.setName(c.getName());
        r.setRoom(c.getRoom());
        r.setAcademicYear(c.getAcademicYear());
        r.setCapacity(c.getCapacity());
        if (c.getClassTeacher() != null)
            r.setClassTeacher(TeacherResponse.from(c.getClassTeacher()));
        return r;
    }
}
