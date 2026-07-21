package com.brainz.sms_backend.Classroom;

import com.brainz.sms_backend.Student.StudentResponse;
import com.brainz.sms_backend.common.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/classrooms")
@RequiredArgsConstructor
public class ClassRoomController {

    private final ClassRoomService classRoomService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ClassRoomResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success("ClassRooms retrieved", classRoomService.getAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ClassRoomResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("ClassRoom retrieved", classRoomService.getById(id)));
    }

    @GetMapping("/{id}/students")
    public ResponseEntity<ApiResponse<List<StudentResponse>>> getStudents(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Students retrieved", classRoomService.getStudents(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ClassRoomResponse>> create(@Valid @RequestBody ClassRoomRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("ClassRoom created", classRoomService.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ClassRoomResponse>> update(@PathVariable Long id,
                                                                  @Valid @RequestBody ClassRoomRequest request) {
        return ResponseEntity.ok(ApiResponse.success("ClassRoom updated", classRoomService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        classRoomService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("ClassRoom deleted", null));
    }
}
