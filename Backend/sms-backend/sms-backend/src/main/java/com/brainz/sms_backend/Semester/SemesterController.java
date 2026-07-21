package com.brainz.sms_backend.Semester;

import com.brainz.sms_backend.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/semesters")
@RequiredArgsConstructor
public class SemesterController {

    private final SemesterService semesterService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<SemesterResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success("Semesters retrieved", semesterService.getAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SemesterResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Semester retrieved", semesterService.getById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SemesterResponse>> create(@Valid @RequestBody SemesterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Semester created", semesterService.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SemesterResponse>> update(@PathVariable Long id,
                                                                 @Valid @RequestBody SemesterRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Semester updated", semesterService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        semesterService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Semester deleted", null));
    }
}
