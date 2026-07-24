package com.brainz.sms_backend.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/*
 * WHY THIS CLASS EXISTS:
 * This is the master configuration for Spring Security.
 * It answers 4 questions:
 *   1. Which endpoints are public and which require a token?
 *   2. How do we load users from the database for authentication?
 *   3. How do we hash passwords?
 *   4. Which origins (URLs) are allowed to call our API (CORS)?
 *
 * @Configuration — tells Spring this class provides @Bean definitions
 * @EnableWebSecurity — activates Spring Security for this application
 * @EnableMethodSecurity — enables @PreAuthorize on controller methods
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final AppUserDetailsService userDetailsService;

    /*
     * THE MAIN SECURITY RULE BOOK
     * Defines which endpoints are open and which require a valid JWT token.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // Disable CSRF (Cross-Site Request Forgery) protection.
                // We don't need it because JWT tokens already protect against this.
                // CSRF is only needed for apps that use cookies for authentication.
                .csrf(AbstractHttpConfigurer::disable)

                // Apply our CORS configuration (defined below)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // Define which URLs require authentication and which are open
                .authorizeHttpRequests(auth -> auth
                        // Anyone can call /api/auth/login and /api/auth/register — no token needed
                        .requestMatchers("/api/auth/**").permitAll()
                        // Swagger UI is also public — useful for testing and demos
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                        // Every other endpoint requires a valid JWT token
                        .anyRequest().authenticated()
                )

                // Use STATELESS sessions — the server does NOT store session data.
                // Each request is self-contained and authenticated via the JWT token alone.
                // This is the correct setting for JWT-based APIs.
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Register our custom authentication provider (defined below)
                .authenticationProvider(authenticationProvider())

                // Add JwtFilter BEFORE Spring's default login filter.
                // This means our JWT check happens before anything else.
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)

                .build();
    }

    /*
     * HOW SPRING SECURITY LOADS AND VERIFIES USERS
     * DaoAuthenticationProvider connects two things:
     *   - WHERE to load users from: our AppUserDetailsService (which reads from the database)
     *   - HOW to verify passwords: BCrypt password encoder
     *
     * This is what the AuthenticationManager uses internally during login.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService); // load users from our DB
        provider.setPasswordEncoder(passwordEncoder());     // check passwords with BCrypt
        return provider;
    }

    /*
     * THE PASSWORD HASHER
     * BCrypt is a one-way hashing algorithm.
     * encode("mypassword") → "$2a$10$N9qo8uLOickgx..." (never the same twice)
     * matches("mypassword", "$2a$10$N9qo8uLOickgx...") → true
     *
     * This @Bean is injected into AuthService to hash passwords during registration,
     * and used by the DaoAuthenticationProvider to verify passwords during login.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
     * THE AUTHENTICATION MANAGER
     * This is the main entry point for authentication.
     * AuthService calls authManager.authenticate(username, password).
     * The manager uses our DaoAuthenticationProvider to do the actual work.
     *
     * We get it from AuthenticationConfiguration — Spring creates it
     * using all the providers we registered (our DaoAuthenticationProvider above).
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /*
     * CORS CONFIGURATION
     * CORS (Cross-Origin Resource Sharing) controls which websites can call our API.
     * Without this, the browser will block all requests from our React frontend
     * because it runs on a different port (5173) than the backend (8080).
     *
     * In production: replace "http://localhost:5173" with your deployed frontend URL.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // Which frontend URLs are allowed to call this API
        config.setAllowedOrigins(List.of(
                "http://localhost:5173",  // React dev server
                "http://localhost:3000"   // fallback for other common ports
        ));

        // Which HTTP methods the frontend can use
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Allow all headers (Authorization, Content-Type, etc.)
        config.setAllowedHeaders(List.of("*"));

        // Allow the frontend to send cookies and auth headers
        config.setAllowCredentials(true);

        // Apply this CORS config to all routes
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
