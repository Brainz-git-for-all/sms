package com.brainz.sms_backend.Student;

import com.brainz.sms_backend.Grade.GradeResponse;
import com.brainz.sms_backend.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<StudentResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success("Students retrieved", studentService.getAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Student retrieved", studentService.getById(id)));
    }

    @GetMapping("/{id}/grades")
    public ResponseEntity<ApiResponse<List<GradeResponse>>> getGrades(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Grades retrieved", studentService.getGrades(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<StudentResponse>> create(@Valid @RequestBody StudentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Student created", studentService.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentResponse>> update(@PathVariable Long id,
                                                               @Valid @RequestBody StudentRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Student updated", studentService.update(id, request)));
    }

    @PutMapping("/{id}/promote")
    public ResponseEntity<ApiResponse<StudentResponse>> promote(@PathVariable Long id,
                                                                @RequestParam Long newClassId) {
        return ResponseEntity.ok(ApiResponse.success("Student promoted", studentService.promote(id, newClassId)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        studentService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Student deleted", null));
    }
}
