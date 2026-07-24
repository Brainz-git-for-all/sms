package com.brainz.sms_backend.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/*
 * WHY THIS CLASS EXISTS:
 * Spring Security does not know how to load users from YOUR database.
 * It only knows how to load something called a "UserDetails" object.
 * This class is the bridge — it implements UserDetailsService, which
 * Spring Security calls when it needs to find a user by username.
 *
 * It is used in TWO places:
 *   1. During login → AuthenticationManager calls loadUserByUsername()
 *      to find the user so it can check the password
 *   2. During every request → JwtFilter calls it to reload the user
 *      from the database using the username inside the JWT token
 *
 * Because AppUser implements UserDetails, we can return it directly.
 * No conversion needed.
 */
@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

    private final AppUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Find the user by username in the database
        // If not found, throw UsernameNotFoundException — Spring Security catches this
        // and converts it to a 401 Unauthorized response
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
}
