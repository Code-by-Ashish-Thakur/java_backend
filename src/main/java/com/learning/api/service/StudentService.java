package com.learning.api.service;

import com.learning.api.entity.Student;
import com.learning.api.repository.StudentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

// ============================================================
// SERVICE LAYER — Contains all BUSINESS LOGIC
// ============================================================
// FLOW: Controller → Service → Repository
//
// Controller: handles HTTP request, calls service
// Service:    applies business logic, calls repository
// Repository: talks to database
//
// @Service tells Spring: "Manage this class as a bean"
// ============================================================

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // Save a new student
    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    // Get all students with search, filter & pagination
    public Page<Student> getAllStudents(String name, String stream, String studentClass, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);

        if (name != null) {
            return studentRepository.findByNameContainingIgnoreCase(name, pageable);
        } else if (stream != null && studentClass != null) {
            return studentRepository.findByStreamAndStudentClass(stream, studentClass, pageable);
        } else if (stream != null) {
            return studentRepository.findByStream(stream, pageable);
        } else if (studentClass != null) {
            return studentRepository.findByStudentClass(studentClass, pageable);
        } else {
            return studentRepository.findAll(pageable);
        }
    }

    // Get student by ID
    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    // Update student by ID
    public Optional<Student> updateStudent(Long id, Student updatedStudent) {
        Optional<Student> student = studentRepository.findById(id);
        if (student.isPresent()) {
            Student existing = student.get();
            existing.setName(updatedStudent.getName());
            existing.setRollNo(updatedStudent.getRollNo());
            existing.setStream(updatedStudent.getStream());
            existing.setStudentClass(updatedStudent.getStudentClass());
            return Optional.of(studentRepository.save(existing));
        }
        return Optional.empty();
    }

    // Delete student by ID
    public Optional<Student> deleteStudent(Long id) {
        Optional<Student> student = studentRepository.findById(id);
        if (student.isPresent()) {
            studentRepository.deleteById(id);
        }
        return student;
    }
}
