package com.brainz.sms_backend.Student;

import com.brainz.sms_backend.Grade.GradeResponse;
import com.brainz.sms_backend.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    // ADMIN and TEACHER can view all students
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<ApiResponse<List<StudentResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success("Students retrieved", studentService.getAll()));
    }

    // ADMIN and TEACHER can view any student by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<ApiResponse<StudentResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Student retrieved", studentService.getById(id)));
    }

    // ADMIN and TEACHER can view any student's grades by ID
    @GetMapping("/{id}/grades")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<ApiResponse<List<GradeResponse>>> getGrades(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Grades retrieved", studentService.getGrades(id)));
    }

    // A STUDENT can view only THEIR OWN grades
    // authentication.getName() returns the logged-in username
    // StudentService uses it to find their studentId and return only their grades
    @GetMapping("/my-grades")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<List<GradeResponse>>> getMyGrades(Authentication authentication) {
        return ResponseEntity.ok(ApiResponse.success("Grades retrieved",
                studentService.getMyGrades(authentication.getName())));
    }

    // Only ADMIN can create students
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<StudentResponse>> create(@Valid @RequestBody StudentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Student created", studentService.create(request)));
    }

    // ADMIN and TEACHER can update student info
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<ApiResponse<StudentResponse>> update(@PathVariable Long id,
                                                               @Valid @RequestBody StudentRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Student updated", studentService.update(id, request)));
    }

    // Only ADMIN can promote a student to a new class
    @PutMapping("/{id}/promote")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<StudentResponse>> promote(@PathVariable Long id,
                                                                @RequestParam Long newClassId) {
        return ResponseEntity.ok(ApiResponse.success("Student promoted", studentService.promote(id, newClassId)));
    }

    // Only ADMIN can delete students
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        studentService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Student deleted", null));
    }
}
