package com.learning.api.service;

import java.util.Optional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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
// CACHING STRATEGY:
//   @Cacheable  → Check Redis first, if found return cached, else query DB and cache it
//   @CacheEvict → Remove stale data from Redis when we create/update/delete
//
// Cache names:
//   "students"    → single student by ID (e.g., students::5)
//   "allStudents" → list results with filters (e.g., allStudents::name_null_stream_Science_...)
// ============================================================

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // CREATE — evict all list caches (new student changes list results)
    @CacheEvict(value = "allStudents", allEntries = true)
    public Student createStudent(StudentRequestDTO requestDTO) {
        Student student = new Student(
                requestDTO.getName(),
                requestDTO.getRollNo(),
                requestDTO.getStream(),
                requestDTO.getStudentClass()
        );
        return studentRepository.save(student);
    }

    // GET ALL — no cache here, caching is done at the controller level
    // (because Page<Student> can't be deserialized from Redis JSON)
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

    // GET BY ID — cache individual student, don't cache when not found
    @Cacheable(value = "students", key = "#id", unless = "#result == null || !#result.isPresent()")
    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    // UPDATE — evict this student's cache AND all list caches
    @Caching(evict = {
        @CacheEvict(value = "students", key = "#id"),
        @CacheEvict(value = "allStudents", allEntries = true)
    })
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

    // DELETE — evict this student's cache AND all list caches
    @Caching(evict = {
        @CacheEvict(value = "students", key = "#id"),
        @CacheEvict(value = "allStudents", allEntries = true)
    })
    public Optional<Student> deleteStudent(Long id) {
        Optional<Student> student = studentRepository.findById(id);
        if (student.isPresent()) {
            studentRepository.deleteById(id);
        }
        return student;
    }
}
