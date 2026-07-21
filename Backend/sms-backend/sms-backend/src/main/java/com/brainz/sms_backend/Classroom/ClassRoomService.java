package com.brainz.sms_backend.Classroom;

import com.brainz.sms_backend.Student.StudentRepository;
import com.brainz.sms_backend.Student.StudentResponse;
import com.brainz.sms_backend.Teacher.Teacher;
import com.brainz.sms_backend.Teacher.TeacherRepository;
import com.brainz.sms_backend.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClassRoomService {

    private final ClassRoomRepository classRoomRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;

    public List<ClassRoomResponse> getAll() {
        return classRoomRepository.findAll().stream()
                .map(ClassRoomResponse::from).collect(Collectors.toList());
    }

    public ClassRoomResponse getById(Long id) {
        return ClassRoomResponse.from(classRoomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ClassRoom not found: " + id)));
    }

    public List<StudentResponse> getStudents(Long classId) {
        classRoomRepository.findById(classId)
                .orElseThrow(() -> new ResourceNotFoundException("ClassRoom not found: " + classId));
        return studentRepository.findByCurrentClassId(classId).stream()
                .map(StudentResponse::from).collect(Collectors.toList());
    }

    public ClassRoomResponse create(ClassRoomRequest request) {
        ClassRoom c = new ClassRoom();
        mapToEntity(request, c);
        return ClassRoomResponse.from(classRoomRepository.save(c));
    }

    public ClassRoomResponse update(Long id, ClassRoomRequest request) {
        ClassRoom c = classRoomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ClassRoom not found: " + id));
        mapToEntity(request, c);
        return ClassRoomResponse.from(classRoomRepository.save(c));
    }

    public void delete(Long id) {
        if (!classRoomRepository.existsById(id))
            throw new ResourceNotFoundException("ClassRoom not found: " + id);
        classRoomRepository.deleteById(id);
    }

    private void mapToEntity(ClassRoomRequest request, ClassRoom c) {
        c.setName(request.getName());
        c.setRoom(request.getRoom());
        c.setAcademicYear(request.getAcademicYear());
        c.setCapacity(request.getCapacity());
        if (request.getClassTeacherId() != null) {
            Teacher teacher = teacherRepository.findById(request.getClassTeacherId())
                    .orElseThrow(() -> new ResourceNotFoundException("Teacher not found: " + request.getClassTeacherId()));
            c.setClassTeacher(teacher);
        }
    }
}
