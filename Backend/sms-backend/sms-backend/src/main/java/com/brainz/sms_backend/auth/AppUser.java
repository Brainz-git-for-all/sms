package com.brainz.sms_backend.auth;

// YOUR TASK — File 5
// This is a JPA Entity AND implements UserDetails from Spring Security
// Annotations on class: @Entity, @Table(name="app_users"), @Data, @Builder, @NoArgsConstructor, @AllArgsConstructor
// Class declaration: public class AppUser implements UserDetails
//
// Fields:
//   Long    id         — @Id, @GeneratedValue(strategy=GenerationType.IDENTITY)
//   String  username   — @Column(unique=true, nullable=false)
//   String  password   — @Column(nullable=false)
//   Role    role       — @Enumerated(EnumType.STRING)
//   boolean enabled    — no annotation needed, default = true
//   Long    studentId  — @Column(nullable=true)  links this account to a Student record (used when role=STUDENT)
//   Long    guardianId — @Column(nullable=true)  links this account to a Guardian record (used when role=GUARDIAN)
//
// WHY these two fields: when a logged-in user has role=STUDENT, we use studentId to find their record
// and only return grades for that student. Same for GUARDIAN — guardianId lets us find their linked children.
//
// Methods required by UserDetails interface (all must be implemented):
//   getAuthorities()          → return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()))
//   getPassword()             → return this.password
//   getUsername()             → return this.username
//   isAccountNonExpired()     → return true
//   isAccountNonLocked()      → return true
//   isCredentialsNonExpired() → return true
//   isEnabled()               → return this.enabled


import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter                          // Lombok: generates getters for all fields
@Setter                          // Lombok: generates setters for all fields
@Builder                         // Lombok: enables AppUser.builder()...build() pattern
@NoArgsConstructor               // Lombok: generates empty constructor — JPA needs this to create objects from DB rows
@AllArgsConstructor              // Lombok: generates constructor with all fields — @Builder needs this internally
@Entity                          // JPA: this class maps to a database table
@Table(name = "app_users")       // JPA: the table name in PostgreSQL will be "app_users"
public class AppUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column( nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean enabled;

    private Long studentId;

    private Long guardianId;



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public  String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
