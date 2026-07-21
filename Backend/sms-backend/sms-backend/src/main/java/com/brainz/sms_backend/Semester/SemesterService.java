package com.brainz.sms_backend.Semester;

import com.brainz.sms_backend.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SemesterService {

    private final SemesterRepository semesterRepository;

    public List<SemesterResponse> getAll() {
        return semesterRepository.findAll().stream()
                .map(SemesterResponse::from).collect(Collectors.toList());
    }

    public SemesterResponse getById(Long id) {
        return SemesterResponse.from(semesterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Semester not found: " + id)));
    }

    public SemesterResponse create(SemesterRequest request) {
        Semester s = new Semester();
        mapToEntity(request, s);
        return SemesterResponse.from(semesterRepository.save(s));
    }

    public SemesterResponse update(Long id, SemesterRequest request) {
        Semester s = semesterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Semester not found: " + id));
        mapToEntity(request, s);
        return SemesterResponse.from(semesterRepository.save(s));
    }

    public void delete(Long id) {
        if (!semesterRepository.existsById(id))
            throw new ResourceNotFoundException("Semester not found: " + id);
        semesterRepository.deleteById(id);
    }

    private void mapToEntity(SemesterRequest r, Semester s) {
        s.setTermName(r.getTermName());
        s.setAcademicYear(r.getAcademicYear());
        s.setStartDate(r.getStartDate());
        s.setEndDate(r.getEndDate());
    }
}
