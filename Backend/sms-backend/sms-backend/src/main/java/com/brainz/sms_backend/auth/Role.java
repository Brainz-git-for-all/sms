package com.brainz.sms_backend.auth;

// YOUR TASK — File 1
// Type: enum (not a class)
// Values: ADMIN, TEACHER, STUDENT
// No fields, no methods — just the three names

public enum Role {
    ADMIN, TEACHER, STUDENT, GUARDIAN
    // TODO: add ADMIN, TEACHER, STUDENT, GUARDIAN here
    // GUARDIAN = a parent/guardian who can view their linked children's grades
    // STUDENT  = a student who can only view their own grades
}
