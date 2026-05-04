package com.learning.api.controller;

import java.util.LinkedHashMap;
import java.util.Map;
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
    public Map<String, Object> createStudent(@RequestBody Student student) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Student saved successfully");
        response.put("data", studentService.createStudent(student));
        return response;
    }

    // GET /api/students (with search, filter & pagination)
    @GetMapping("/api/students")
    public Map<String, Object> getAllStudents(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String stream,
            @RequestParam(required = false) String studentClass,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Map<String, Object> response = new LinkedHashMap<>();
        Page<Student> studentPage = studentService.getAllStudents(name, stream, studentClass, page, size);

        response.put("message", "Found " + studentPage.getTotalElements() + " student(s)");
        response.put("data", studentPage.getContent());
        response.put("currentPage", studentPage.getNumber());
        response.put("totalPages", studentPage.getTotalPages());
        response.put("totalItems", studentPage.getTotalElements());
        return response;
    }

    // GET /api/students/{id}
    @GetMapping("/api/students/{id}")
    public Map<String, Object> getStudentById(@PathVariable Long id) {
        Map<String, Object> response = new LinkedHashMap<>();
        Optional<Student> student = studentService.getStudentById(id);
        if (student.isPresent()) {
            response.put("message", "Student found successfully");
            response.put("data", student.get());
        } else {
            response.put("message", "Student not found with id: " + id);
            response.put("data", null);
        }
        return response;
    }

    // PUT /api/students/{id}
    @PutMapping("/api/students/{id}")
    public Map<String, Object> updateStudent(@PathVariable Long id, @RequestBody Student updatedStudent) {
        Map<String, Object> response = new LinkedHashMap<>();
        Optional<Student> student = studentService.updateStudent(id, updatedStudent);
        if (student.isPresent()) {
            response.put("message", "Student updated successfully");
            response.put("data", student.get());
        } else {
            response.put("message", "Student not found with id: " + id);
            response.put("data", null);
        }
        return response;
    }

    // DELETE /api/students/{id}
    @DeleteMapping("/api/students/{id}")
    public Map<String, Object> deleteStudent(@PathVariable Long id) {
        Map<String, Object> response = new LinkedHashMap<>();
        Optional<Student> student = studentService.deleteStudent(id);
        if (student.isPresent()) {
            response.put("message", "Student deleted successfully");
            response.put("data", student.get());
        } else {
            response.put("message", "Student not found with id: " + id);
            response.put("data", null);
        }
        return response;
    }
}
