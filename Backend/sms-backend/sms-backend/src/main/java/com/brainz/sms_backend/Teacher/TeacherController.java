package com.brainz.sms_backend.Teacher;

import com.brainz.sms_backend.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<TeacherResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success("Teachers retrieved", teacherService.getAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TeacherResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Teacher retrieved", teacherService.getById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TeacherResponse>> create(@Valid @RequestBody TeacherRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Teacher created", teacherService.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TeacherResponse>> update(@PathVariable Long id,
                                                               @Valid @RequestBody TeacherRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Teacher updated", teacherService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        teacherService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Teacher deleted", null));
    }
}
