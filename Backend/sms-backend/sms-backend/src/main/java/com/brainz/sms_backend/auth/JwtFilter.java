package com.brainz.sms_backend.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/*
 * WHY THIS CLASS EXISTS:
 * This filter runs ONCE on every single HTTP request before any controller runs.
 * Its job is to check the Authorization header, validate the JWT token,
 * and tell Spring Security who is making the request.
 *
 * It extends OncePerRequestFilter — a Spring class that guarantees
 * this filter runs exactly once per request (not multiple times).
 *
 * FLOW:
 *   Request arrives
 *     → No Authorization header? → skip (let the request through, Spring Security
 *       will block it if the endpoint requires authentication)
 *     → Has "Bearer <token>"? → extract token → get username from token
 *       → load user from DB → validate token → set SecurityContext
 *     → Pass request to the next filter / controller
 */
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final AppUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // Step 1: Read the Authorization header from the request
        // It should look like: "Bearer eyJhbGciOiJIUzI1NiJ9..."
        final String authHeader = request.getHeader("Authorization");

        // Step 2: If there is no header, or it doesn't start with "Bearer ",
        // this is either a public endpoint (like /api/auth/login) or an invalid request.
        // Just pass it through — SecurityConfig will decide if it needs authentication.
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return; // stop here — don't run the rest of this filter
        }

        // Step 3: Extract the token by removing "Bearer " (7 characters) from the start
        final String token = authHeader.substring(7);

        // Step 4: Extract the username that is stored inside the token
        // If the token is malformed or tampered with, jwtUtil will throw an exception
        final String username = jwtUtil.extractUsername(token);

        // Step 5: Only proceed if we got a username AND the user isn't already authenticated
        // SecurityContextHolder.getContext().getAuthentication() == null means
        // this request hasn't been authenticated yet in this filter chain
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Step 6: Load the full AppUser from the database using the username
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Step 7: Check the token is valid (username matches + not expired)
            if (jwtUtil.isTokenValid(token, userDetails)) {

                // Step 8: Create an authentication object — this is what Spring Security
                // stores to represent "who is logged in for this request"
                // Parameters: (the user, credentials=null because JWT, the user's roles)
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities() // contains "ROLE_ADMIN", "ROLE_TEACHER" etc.
                        );

                // Step 9: Attach extra request details (IP address, session info) to the auth token
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Step 10: Store the authentication in the SecurityContext
                // This is the "notice board" — once this is set, the rest of the application
                // knows who is making this request and what role they have
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Step 11: Pass the request to the next filter or controller
        filterChain.doFilter(request, response);
    }
}
