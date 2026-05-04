package com.learning.api.repository;

import com.learning.api.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Page<Employee> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Employee> findByDepartment(String department, Pageable pageable);

    Page<Employee> findByDesignation(String designation, Pageable pageable);

    Page<Employee> findByDepartmentAndDesignation(String department, String designation, Pageable pageable);
}
