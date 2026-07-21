package com.brainz.sms_backend.Subject;

import com.brainz.sms_backend.Teacher.TeacherResponse;
import lombok.Data;

@Data
public class SubjectResponse {
    private Long id;
    private String name;
    private String code;
    private String description;
    private TeacherResponse teacher;

    public static SubjectResponse from(Subject s) {
        SubjectResponse r = new SubjectResponse();
        r.setId(s.getId());
        r.setName(s.getName());
        r.setCode(s.getCode());
        r.setDescription(s.getDescription());
        if (s.getTeacher() != null)
            r.setTeacher(TeacherResponse.from(s.getTeacher()));
        return r;
    }
}
