package com.brainz.sms_backend.Teacher;

import com.brainz.sms_backend.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherRepository teacherRepository;

    public List<TeacherResponse> getAll() {
        return teacherRepository.findAll().stream()
                .map(TeacherResponse::from).collect(Collectors.toList());
    }

    public TeacherResponse getById(Long id) {
        return TeacherResponse.from(teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found: " + id)));
    }

    public TeacherResponse create(TeacherRequest request) {
        Teacher t = new Teacher();
        mapToEntity(request, t);
        return TeacherResponse.from(teacherRepository.save(t));
    }

    public TeacherResponse update(Long id, TeacherRequest request) {
        Teacher t = teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found: " + id));
        mapToEntity(request, t);
        return TeacherResponse.from(teacherRepository.save(t));
    }

    public void delete(Long id) {
        if (!teacherRepository.existsById(id))
            throw new ResourceNotFoundException("Teacher not found: " + id);
        teacherRepository.deleteById(id);
    }

    private void mapToEntity(TeacherRequest r, Teacher t) {
        t.setFirstName(r.getFirstName());
        t.setLastName(r.getLastName());
        t.setEmail(r.getEmail());
        t.setPhone(r.getPhone());
        t.setQualification(r.getQualification());
        t.setSpecialization(r.getSpecialization());
    }
}
