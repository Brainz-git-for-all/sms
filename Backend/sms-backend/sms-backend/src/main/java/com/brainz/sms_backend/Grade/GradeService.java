package com.brainz.sms_backend.Grade;

import com.brainz.sms_backend.Semester.Semester;
import com.brainz.sms_backend.Semester.SemesterRepository;
import com.brainz.sms_backend.Student.Student;
import com.brainz.sms_backend.Student.StudentRepository;
import com.brainz.sms_backend.Subject.Subject;
import com.brainz.sms_backend.Subject.SubjectRepository;
import com.brainz.sms_backend.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GradeService {

    private final GradeRepository gradeRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final SemesterRepository semesterRepository;

    public List<GradeResponse> getAll() {
        return gradeRepository.findAll().stream()
                .map(GradeResponse::from).collect(Collectors.toList());
    }

    public GradeResponse getById(Long id) {
        return GradeResponse.from(gradeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Grade not found: " + id)));
    }

    public List<GradeResponse> getByStudent(Long studentId) {
        return gradeRepository.findByStudentId(studentId).stream()
                .map(GradeResponse::from).collect(Collectors.toList());
    }

    public List<GradeResponse> getByStudentAndSemester(Long studentId, Long semesterId) {
        return gradeRepository.findByStudentIdAndSemesterId(studentId, semesterId).stream()
                .map(GradeResponse::from).collect(Collectors.toList());
    }

    public GradeResponse create(GradeRequest request) {
        Grade grade = new Grade();
        mapToEntity(request, grade);
        calculate(grade);
        return GradeResponse.from(gradeRepository.save(grade));
    }

    public GradeResponse update(Long id, GradeRequest request) {
        Grade grade = gradeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Grade not found: " + id));
        mapToEntity(request, grade);
        calculate(grade);
        return GradeResponse.from(gradeRepository.save(grade));
    }

    public void delete(Long id) {
        if (!gradeRepository.existsById(id))
            throw new ResourceNotFoundException("Grade not found: " + id);
        gradeRepository.deleteById(id);
    }

    private void mapToEntity(GradeRequest request, Grade grade) {
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + request.getStudentId()));
        Subject subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found: " + request.getSubjectId()));
        Semester semester = semesterRepository.findById(request.getSemesterId())
                .orElseThrow(() -> new ResourceNotFoundException("Semester not found: " + request.getSemesterId()));
        grade.setStudent(student);
        grade.setSubject(subject);
        grade.setSemester(semester);
        grade.setTest1(request.getTest1());
        grade.setTest2(request.getTest2());
        grade.setExams(request.getExams());
    }

    private void calculate(Grade grade) {
        double total = grade.getTest1() + grade.getTest2() + grade.getExams();
        grade.setTotal(total);
        if (total >= 80)      { grade.setGradeLetter("A"); grade.setRemarks("DISTINCTION"); }
        else if (total >= 70) { grade.setGradeLetter("B"); grade.setRemarks("PASS"); }
        else if (total >= 60) { grade.setGradeLetter("C"); grade.setRemarks("PASS"); }
        else if (total >= 50) { grade.setGradeLetter("D"); grade.setRemarks("PASS"); }
        else                  { grade.setGradeLetter("F"); grade.setRemarks("FAIL"); }
    }
}
