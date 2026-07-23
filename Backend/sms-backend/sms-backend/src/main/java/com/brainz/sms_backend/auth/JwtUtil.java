package com.brainz.sms_backend.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expiration;

    // ─── I WROTE THIS — study the pattern, then write the two methods below ───

    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername())        // puts username inside the token
                .issuedAt(new Date(System.currentTimeMillis()))          // when the token was created
                .expiration(new Date(System.currentTimeMillis() + expiration)) // when it expires
                .signWith(getSigningKey())                 // signs it with our secret key
                .compact();                                // converts to the final string
    }

    // helper — converts the secret string from properties into a real cryptographic key
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // helper — reads all the data (claims) out of a token
    // used by extractUsername() and isTokenValid() below
    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())   // uses our secret key to verify the signature
                .build()
                .parseSignedClaims(token)      // decodes the token
                .getPayload();                 // returns the data inside
    }

    // ─── YOUR TASK 1: extractUsername(String token) → String ───────────────────
    // Use extractClaims(token) to get the claims, then call .getSubject() on them.
    // getSubject() returns whatever was set as .subject() in generateToken() — the username.
    // Pattern: return extractClaims(token).getSubject();

    public String extractUsername(String token) {
        // TODO: return the subject (username) from extractClaims(token)
        return extractClaims(token).getSubject();
    }

    // ─── YOUR TASK 2: isTokenValid(String token, UserDetails userDetails) → boolean ───
    // This method checks TWO things:
    //   1. Does the username inside the token match the userDetails username?
    //      → extractUsername(token).equals(userDetails.getUsername())
    //   2. Has the token NOT expired yet?
    //      → use extractClaims(token).getExpiration().before(new Date())
    //         that gives you true if it IS expired — so you want the opposite: !isExpired
    // Return true only if BOTH conditions are true (use &&)

    public boolean isTokenValid(String token, UserDetails userDetails) {

       String username = extractUsername(token);

        return ((username.equals(userDetails.getUsername())) && !extractClaims(token).getExpiration().before(new Date()));
    }
}
