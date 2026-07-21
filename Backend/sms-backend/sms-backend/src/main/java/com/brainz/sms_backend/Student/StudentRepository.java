package com.brainz.sms_backend.Student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByCurrentClassId(Long classId);
    List<Student> findByGuardianId(Long guardianId);
    List<Student> findByStatus(String status);
    Optional<Student> findByEmail(String email);
}
