package com.brainz.sms_backend.Classroom;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ClassRoomRequest {
    @NotBlank(message = "Class name is required")
    private String name;
    private String room;
    @NotBlank(message = "Academic year is required")
    private String academicYear;
    private Integer capacity;
    private ClassRoomStatus status;
    private ActiveStatus active;
    private Long classTeacherId;
}
