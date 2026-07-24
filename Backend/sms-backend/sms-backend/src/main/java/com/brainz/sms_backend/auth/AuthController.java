package com.brainz.sms_backend.auth;

// YOUR TASK — File 7
// Same pattern as StudentController. Two POST endpoints.
// Class annotations: @RestController, @RequestMapping("/api/auth"), @RequiredArgsConstructor
// Inject: private final AuthService authService;
//
// Methods:
//
// 1. login()
//    @PostMapping("/login")
//    parameter: @Valid @RequestBody LoginRequest request
//    returns:   ResponseEntity<AuthResponse>
//    body:      return ResponseEntity.ok(authService.login(request));
//
// 2. register()
//    @PostMapping("/register")
//    parameter: @Valid @RequestBody RegisterRequest request
//    returns:   ResponseEntity<AuthResponse>
//    body:      return ResponseEntity.status(201).body(authService.register(request));

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // TODO: implement login() method
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    // TODO: implement register() method
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(201).body(authService.register(request));
    }
}
