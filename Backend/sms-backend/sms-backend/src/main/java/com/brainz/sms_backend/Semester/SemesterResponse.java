package com.brainz.sms_backend.Semester;

import lombok.Data;
import java.time.LocalDate;

@Data
public class SemesterResponse {
    private Long id;
    private String termName;
    private String academicYear;
    private LocalDate startDate;
    private LocalDate endDate;

    public static SemesterResponse from(Semester s) {
        SemesterResponse r = new SemesterResponse();
        r.setId(s.getId());
        r.setTermName(s.getTermName());
        r.setAcademicYear(s.getAcademicYear());
        r.setStartDate(s.getStartDate());
        r.setEndDate(s.getEndDate());
        return r;
    }
}
