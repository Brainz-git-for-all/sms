package com.brainz.sms_backend.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/*
 * WHY THIS CLASS EXISTS:
 * @PreAuthorize can only call Spring beans registered as @Service or @Component.
 * This class provides methods that @PreAuthorize uses to answer the question:
 * "does the currently logged-in user OWN this resource?"
 *
 * EXAMPLE USAGE on an endpoint:
 *
 *   @PreAuthorize("hasRole('ADMIN') or @securityService.isStudentOwner(authentication, #studentId)")
 *
 * The "#studentId" refers to the method parameter named studentId.
 * "authentication" is automatically injected by Spring Security.
 * "@securityService" is the bean name of this class (lowercase class name by default).
 *
 * This prevents a student with id=5 from accessing the grades of student with id=7.
 */
@Service
@RequiredArgsConstructor
public class SecurityService {

    private final AppUserRepository userRepository;

    /*
     * Returns true if the logged-in user's linked studentId matches the requested studentId.
     * Used to ensure a student can only access THEIR OWN grades.
     *
     * How it works:
     *   1. Get the username of the logged-in user from the Authentication object
     *   2. Load their AppUser from the database
     *   3. Compare their studentId field to the requested studentId
     */
    public boolean isStudentOwner(Authentication authentication, Long studentId) {
        String username = authentication.getName(); // gets the logged-in username
        return userRepository.findByUsername(username)
                .map(user -> studentId.equals(user.getStudentId()))
                .orElse(false);
    }

    /*
     * Returns true if the logged-in user's linked guardianId matches the requested guardianId.
     * Used to ensure a guardian can only access THEIR OWN children's grades.
     */
    public boolean isGuardianOwner(Authentication authentication, Long guardianId) {
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .map(user -> guardianId.equals(user.getGuardianId()))
                .orElse(false);
    }
}
