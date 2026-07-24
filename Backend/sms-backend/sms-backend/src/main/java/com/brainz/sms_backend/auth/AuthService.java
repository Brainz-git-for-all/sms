package com.brainz.sms_backend.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AppUserRepository userRepository;
    private final JwtUtil jwtUtil;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;

    // ─── I WROTE THIS — read every line and understand it before writing register() ───

    public AuthResponse login(LoginRequest request) {

        // Step 1: ask Spring Security to verify the username and password
        // This internally loads the user from DB and checks the BCrypt hash
        // If wrong credentials → throws BadCredentialsException → controller returns 401
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // Step 2: credentials were correct — now load the full AppUser from the database
        AppUser user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Step 3: generate a JWT token for this user
        String token = jwtUtil.generateToken(user);

        // Step 4: build and return the response the frontend needs
        return AuthResponse.builder()
                .token(token)
                .username(user.getUsername())
                .role(user.getRole().name())  // .name() converts enum → String e.g. "ADMIN"
                .build();
    }

    // ─── YOUR TASK: write register() below ──────────────────────────────────────
    // Method signature: public AuthResponse register(RegisterRequest request)
    //
    // Step 1 — check if username is already taken:
    //   if (userRepository.existsByUsername(request.getUsername()))
    //       throw new RuntimeException("Username already taken");
    //
    // Step 2 — build the new AppUser using the builder (same pattern as login's return):
    //   AppUser user = AppUser.builder()
    //       .username(request.getUsername())
    //       .password(passwordEncoder.encode(request.getPassword()))  ← ALWAYS hash the password
    //       .role(request.getRole())
    //       .studentId(request.getStudentId())
    //       .guardianId(request.getGuardianId())
    //       .enabled(true)
    //       .build();
    //
    // Step 3 — save to database:
    //   AppUser savedUser = userRepository.save(user);
    //
    // Step 4 — generate token:
    //   String token = jwtUtil.generateToken(savedUser);
    //
    // Step 5 — return AuthResponse (same pattern as login):
    //   return AuthResponse.builder()
    //       .token(token)
    //       .username(savedUser.getUsername())
    //       .role(savedUser.getRole().name())
    //       .build();

    public AuthResponse register(RegisterRequest request) {

        // Step 1: check if the username is already taken
        // existsByUsername() returns true if a row exists in app_users with that username
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already taken: " + request.getUsername());
        }

        // Step 2: build the new AppUser object
        // IMPORTANT: never store the raw password — always hash it with passwordEncoder.encode()
        // BCrypt turns "mypassword" into something like "$2a$10$N9qo8uLO..." — irreversible
        AppUser user = AppUser.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .studentId(request.getStudentId())   // null for ADMIN and TEACHER
                .guardianId(request.getGuardianId()) // null for ADMIN, TEACHER and STUDENT
                .enabled(true)
                .build();

        // Step 3: save to database — returns the saved entity with the generated id
        AppUser savedUser = userRepository.save(user);

        // Step 4: generate a JWT token for the newly registered user
        // They are immediately logged in after registering
        String token = jwtUtil.generateToken(savedUser);

        // Step 5: return the response the frontend needs
        return AuthResponse.builder()
                .token(token)
                .username(savedUser.getUsername())
                .role(savedUser.getRole().name()) // .name() converts Role.ADMIN → "ADMIN"
                .build();
    }
}
