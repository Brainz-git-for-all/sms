package com.brainz.sms_backend.Grade;

import com.brainz.sms_backend.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grades")
@RequiredArgsConstructor
public class GradeController {

    private final GradeService gradeService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<GradeResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success("Grades retrieved", gradeService.getAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GradeResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Grade retrieved", gradeService.getById(id)));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<ApiResponse<List<GradeResponse>>> getByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(ApiResponse.success("Grades retrieved", gradeService.getByStudent(studentId)));
    }

    @GetMapping("/student/{studentId}/semester/{semesterId}")
    public ResponseEntity<ApiResponse<List<GradeResponse>>> getByStudentAndSemester(
            @PathVariable Long studentId, @PathVariable Long semesterId) {
        return ResponseEntity.ok(ApiResponse.success("Grades retrieved",
                gradeService.getByStudentAndSemester(studentId, semesterId)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<GradeResponse>> create(@Valid @RequestBody GradeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Grade created", gradeService.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<GradeResponse>> update(@PathVariable Long id,
                                                              @Valid @RequestBody GradeRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Grade updated", gradeService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        gradeService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Grade deleted", null));
    }
}
