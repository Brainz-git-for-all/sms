package com.brainz.sms_backend.Guardian;

import com.brainz.sms_backend.Grade.GradeRepository;
import com.brainz.sms_backend.Grade.GradeResponse;
import com.brainz.sms_backend.Student.StudentRepository;
import com.brainz.sms_backend.auth.AppUserRepository;
import com.brainz.sms_backend.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GuardianService {

    private final GuardianRepository guardianRepository;
    private final AppUserRepository appUserRepository;
    private final StudentRepository studentRepository;
    private final GradeRepository gradeRepository;

    public List<GuardianResponse> getAll() {
        return guardianRepository.findAll().stream()
                .map(GuardianResponse::from).collect(Collectors.toList());
    }

    public GuardianResponse getById(Long id) {
        return GuardianResponse.from(guardianRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Guardian not found: " + id)));
    }

    public GuardianResponse create(GuardianRequest request) {
        Guardian g = new Guardian();
        mapToEntity(request, g);
        return GuardianResponse.from(guardianRepository.save(g));
    }

    public GuardianResponse update(Long id, GuardianRequest request) {
        Guardian g = guardianRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Guardian not found: " + id));
        mapToEntity(request, g);
        return GuardianResponse.from(guardianRepository.save(g));
    }

    public void delete(Long id) {
        if (!guardianRepository.existsById(id))
            throw new ResourceNotFoundException("Guardian not found: " + id);
        guardianRepository.deleteById(id);
    }

    public List<GradeResponse> getChildrenGrades(String username) {
        Long guardianId = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username))
                .getGuardianId();
        if (guardianId == null)
            throw new ResourceNotFoundException("No guardian profile linked to account: " + username);
        return studentRepository.findByGuardianId(guardianId).stream()
                .flatMap(student -> gradeRepository.findByStudentId(student.getId()).stream())
                .map(GradeResponse::from)
                .collect(Collectors.toList());
    }

    private void mapToEntity(GuardianRequest r, Guardian g) {
        g.setName(r.getName());
        g.setPhone(r.getPhone());
        g.setEmail(r.getEmail());
        g.setAddress(r.getAddress());
        g.setRelationship(r.getRelationship());
        g.setOccupation(r.getOccupation());
    }
}
