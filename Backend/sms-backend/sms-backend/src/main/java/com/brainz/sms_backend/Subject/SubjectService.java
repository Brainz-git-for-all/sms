package com.brainz.sms_backend.Subject;

import com.brainz.sms_backend.Teacher.Teacher;
import com.brainz.sms_backend.Teacher.TeacherRepository;
import com.brainz.sms_backend.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;

    public List<SubjectResponse> getAll() {
        return subjectRepository.findAll().stream()
                .map(SubjectResponse::from).collect(Collectors.toList());
    }

    public SubjectResponse getById(Long id) {
        return SubjectResponse.from(subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found: " + id)));
    }

    public SubjectResponse create(SubjectRequest request) {
        Subject s = new Subject();
        mapToEntity(request, s);
        return SubjectResponse.from(subjectRepository.save(s));
    }

    public SubjectResponse update(Long id, SubjectRequest request) {
        Subject s = subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found: " + id));
        mapToEntity(request, s);
        return SubjectResponse.from(subjectRepository.save(s));
    }

    public void delete(Long id) {
        if (!subjectRepository.existsById(id))
            throw new ResourceNotFoundException("Subject not found: " + id);
        subjectRepository.deleteById(id);
    }

    private void mapToEntity(SubjectRequest request, Subject s) {
        s.setName(request.getName());
        s.setCode(request.getCode());
        s.setDescription(request.getDescription());
        if (request.getTeacherId() != null) {
            Teacher t = teacherRepository.findById(request.getTeacherId())
                    .orElseThrow(() -> new ResourceNotFoundException("Teacher not found: " + request.getTeacherId()));
            s.setTeacher(t);
        } else {
            s.setTeacher(null);
        }
    }
}
