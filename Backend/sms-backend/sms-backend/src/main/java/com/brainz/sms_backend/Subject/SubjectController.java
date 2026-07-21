package com.brainz.sms_backend.Subject;

import com.brainz.sms_backend.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subjects")
@RequiredArgsConstructor
public class SubjectController {

    private final SubjectService subjectService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<SubjectResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success("Subjects retrieved", subjectService.getAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SubjectResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Subject retrieved", subjectService.getById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SubjectResponse>> create(@Valid @RequestBody SubjectRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Subject created", subjectService.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SubjectResponse>> update(@PathVariable Long id,
                                                               @Valid @RequestBody SubjectRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Subject updated", subjectService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        subjectService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Subject deleted", null));
    }
}
