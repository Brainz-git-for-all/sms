package com.brainz.sms_backend.Student;

import com.brainz.sms_backend.Classroom.ClassRoom;
import com.brainz.sms_backend.Classroom.ClassRoomRepository;
import com.brainz.sms_backend.Grade.GradeRepository;
import com.brainz.sms_backend.Grade.GradeResponse;
import com.brainz.sms_backend.Guardian.Guardian;
import com.brainz.sms_backend.Guardian.GuardianRepository;
import com.brainz.sms_backend.auth.AppUserRepository;
import com.brainz.sms_backend.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final GuardianRepository guardianRepository;
    private final ClassRoomRepository classRoomRepository;
    private final GradeRepository gradeRepository;
    private final AppUserRepository appUserRepository;

    public List<StudentResponse> getAll() {
        return studentRepository.findAll().stream()
                .map(StudentResponse::from).collect(Collectors.toList());
    }

    public StudentResponse getById(Long id) {
        return StudentResponse.from(studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + id)));
    }

    public List<GradeResponse> getGrades(Long studentId) {
        studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + studentId));
        return gradeRepository.findByStudentId(studentId).stream()
                .map(GradeResponse::from).collect(Collectors.toList());
    }

    public List<GradeResponse> getMyGrades(String username) {
        Long studentId = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username))
                .getStudentId();
        if (studentId == null)
            throw new ResourceNotFoundException("No student profile linked to account: " + username);
        return gradeRepository.findByStudentId(studentId).stream()
                .map(GradeResponse::from).collect(Collectors.toList());
    }

    public StudentResponse create(StudentRequest request) {
        Student s = new Student();
        mapToEntity(request, s);
        if (s.getEnrollmentDate() == null) s.setEnrollmentDate(LocalDate.now());
        if (s.getStatus() == null) s.setStatus(StudentStatus.ACTIVE);
        return StudentResponse.from(studentRepository.save(s));
    }

    public StudentResponse update(Long id, StudentRequest request) {
        Student s = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + id));
        mapToEntity(request, s);
        return StudentResponse.from(studentRepository.save(s));
    }

    public StudentResponse promote(Long studentId, Long newClassId) {
        Student s = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + studentId));
        ClassRoom newClass = classRoomRepository.findById(newClassId)
                .orElseThrow(() -> new ResourceNotFoundException("ClassRoom not found: " + newClassId));
        s.setCurrentClass(newClass);
        return StudentResponse.from(studentRepository.save(s));
    }

    public void delete(Long id) {
        if (!studentRepository.existsById(id))
            throw new ResourceNotFoundException("Student not found: " + id);
        studentRepository.deleteById(id);
    }

    private void mapToEntity(StudentRequest request, Student s) {
        s.setFirstName(request.getFirstName());
        s.setLastName(request.getLastName());
        s.setEmail(request.getEmail());
        s.setPhone(request.getPhone());
        s.setGender(request.getGender());
        s.setDateOfBirth(request.getDateOfBirth());
        s.setEnrollmentDate(request.getEnrollmentDate());
        s.setStatus(request.getStatus());
        if (request.getGuardianId() != null) {
            Guardian g = guardianRepository.findById(request.getGuardianId())
                    .orElseThrow(() -> new ResourceNotFoundException("Guardian not found: " + request.getGuardianId()));
            s.setGuardian(g);
        }
        if (request.getCurrentClassId() != null) {
            ClassRoom c = classRoomRepository.findById(request.getCurrentClassId())
                    .orElseThrow(() -> new ResourceNotFoundException("ClassRoom not found: " + request.getCurrentClassId()));
            s.setCurrentClass(c);
        }
    }
}
