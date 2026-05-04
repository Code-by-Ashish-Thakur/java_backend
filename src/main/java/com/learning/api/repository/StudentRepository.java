package com.learning.api.repository;

import com.learning.api.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// ============================================================
// HOW PAGINATION WORKS:
// ============================================================
// Without pagination: findAll() → returns ALL 1000 students at once (slow!)
// With pagination:    findAll(pageable) → returns only 10 students per page (fast!)
//
// Pageable = an object that holds page number + page size
// Page     = the result that contains data + total pages + total items
//
// FLOW:
//   1. User sends: GET /api/students?page=0&size=5
//   2. Controller creates: Pageable pageable = PageRequest.of(0, 5)
//   3. Repository runs:    SELECT * FROM students LIMIT 5 OFFSET 0
//   4. Returns Page object with data + pagination info
// ============================================================

public interface StudentRepository extends JpaRepository<Student, Long> {

    // --- Without pagination (returns List) ---
    List<Student> findByNameContainingIgnoreCase(String name);
    List<Student> findByStream(String stream);
    List<Student> findByStudentClass(String studentClass);
    List<Student> findByStreamAndStudentClass(String stream, String studentClass);

    // --- With pagination (returns Page) ---
    // Same methods but accept Pageable → returns paginated results
    Page<Student> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Student> findByStream(String stream, Pageable pageable);
    Page<Student> findByStudentClass(String studentClass, Pageable pageable);
    Page<Student> findByStreamAndStudentClass(String stream, String studentClass, Pageable pageable);
}
