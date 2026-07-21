package com.brainz.sms_backend.Grade;

import com.brainz.sms_backend.Semester.SemesterResponse;
import com.brainz.sms_backend.Student.StudentResponse;
import com.brainz.sms_backend.Subject.SubjectResponse;
import lombok.Data;

@Data
public class GradeResponse {
    private Long id;
    private Double test1;
    private Double test2;
    private Double exams;
    private Double total;
    private String gradeLetter;
    private String remarks;
    private StudentResponse student;
    private SubjectResponse subject;
    private SemesterResponse semester;

    public static GradeResponse from(Grade g) {
        GradeResponse r = new GradeResponse();
        r.setId(g.getId());
        r.setTest1(g.getTest1());
        r.setTest2(g.getTest2());
        r.setExams(g.getExams());
        r.setTotal(g.getTotal());
        r.setGradeLetter(g.getGradeLetter());
        r.setRemarks(g.getRemarks());
        if (g.getStudent() != null) r.setStudent(StudentResponse.from(g.getStudent()));
        if (g.getSubject() != null) r.setSubject(SubjectResponse.from(g.getSubject()));
        if (g.getSemester() != null) r.setSemester(SemesterResponse.from(g.getSemester()));
        return r;
    }
}
