package com.learning.api.controller;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.learning.api.dto.StudentRequestDTO;
import com.learning.api.dto.StudentResponseDTO;
import com.learning.api.dto.StudentPageResponseDTO;
import com.learning.api.entity.Student;
import com.learning.api.service.StudentService;

// ============================================================
// CONTROLLER LAYER — Handles HTTP requests only
// ============================================================
// BEFORE (2-layer):  Controller → Repository (controller did everything)
// NOW    (3-layer):  Controller → Service → Repository
//
// Controller's ONLY job: receive request → call service → send response
// All business logic is now in StudentService
// ============================================================

@RestController
public class StudentController {

    private final StudentService studentService;

    // Now we inject StudentService instead of StudentRepository
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @RequestMapping("/")
    public String getHome() {
        return "I am from home";
    }

    @RequestMapping("/about")
    public String aboutStudent() {
        return "I am from about and total number of studet today is 100";
    }

    // POST /api/students
    @PostMapping("/api/students")
    public StudentResponseDTO createStudent(@RequestBody StudentRequestDTO requestDTO) {
        return new StudentResponseDTO("Student saved successfully", studentService.createStudent(requestDTO));
    }

    // GET /api/students (with search, filter & pagination)
    @GetMapping("/api/students")
    public StudentPageResponseDTO getAllStudents(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String stream,
            @RequestParam(required = false) String studentClass,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Student> studentPage = studentService.getAllStudents(name, stream, studentClass, page, size);

        return new StudentPageResponseDTO(
                "Found " + studentPage.getTotalElements() + " student(s)",
                studentPage.getContent(),
                studentPage.getNumber(),
                studentPage.getTotalPages(),
                studentPage.getTotalElements()
        );
    }

    // GET /api/students/{id}
    @GetMapping("/api/students/{id}")
    public StudentResponseDTO getStudentById(@PathVariable Long id) {
        Optional<Student> student = studentService.getStudentById(id);
        if (student.isPresent()) {
            return new StudentResponseDTO("Student found successfully", student.get());
        }
        return new StudentResponseDTO("Student not found with id: " + id, null);
    }

    // PUT /api/students/{id}
    @PutMapping("/api/students/{id}")
    public StudentResponseDTO updateStudent(@PathVariable Long id, @RequestBody StudentRequestDTO requestDTO) {
        Optional<Student> student = studentService.updateStudent(id, requestDTO);
        if (student.isPresent()) {
            return new StudentResponseDTO("Student updated successfully", student.get());
        }
        return new StudentResponseDTO("Student not found with id: " + id, null);
    }

    // DELETE /api/students/{id}
    @DeleteMapping("/api/students/{id}")
    public StudentResponseDTO deleteStudent(@PathVariable Long id) {
        Optional<Student> student = studentService.deleteStudent(id);
        if (student.isPresent()) {
            return new StudentResponseDTO("Student deleted successfully", student.get());
        }
        return new StudentResponseDTO("Student not found with id: " + id, null);
    }
}
