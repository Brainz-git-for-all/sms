package com.brainz.sms_backend.Guardian;

import com.brainz.sms_backend.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/guardians")
@RequiredArgsConstructor
public class GuardianController {

    private final GuardianService guardianService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<GuardianResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success("Guardians retrieved", guardianService.getAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GuardianResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Guardian retrieved", guardianService.getById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<GuardianResponse>> create(@Valid @RequestBody GuardianRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Guardian created", guardianService.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<GuardianResponse>> update(@PathVariable Long id,
                                                                @Valid @RequestBody GuardianRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Guardian updated", guardianService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        guardianService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Guardian deleted", null));
    }
}
