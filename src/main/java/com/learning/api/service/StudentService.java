package com.learning.api.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.learning.api.dto.StudentRequestDTO;
import com.learning.api.entity.Student;
import com.learning.api.repository.StudentRepository;

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
    public Student createStudent(StudentRequestDTO requestDTO) {
        Student student = new Student(
                requestDTO.getName(),
                requestDTO.getRollNo(),
                requestDTO.getStream(),
                requestDTO.getStudentClass()
        );
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
    public Optional<Student> updateStudent(Long id, StudentRequestDTO requestDTO) {
        Optional<Student> student = studentRepository.findById(id);
        if (student.isPresent()) {
            Student existing = student.get();
            existing.setName(requestDTO.getName());
            existing.setRollNo(requestDTO.getRollNo());
            existing.setStream(requestDTO.getStream());
            existing.setStudentClass(requestDTO.getStudentClass());
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
