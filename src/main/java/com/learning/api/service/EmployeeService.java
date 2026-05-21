package com.learning.api.service;

import java.util.Optional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.learning.api.dto.EmployeeRequestDTO;
import com.learning.api.entity.Employee;
import com.learning.api.repository.EmployeeRepository;

// ============================================================
// EMPLOYEE SERVICE — Same caching pattern as StudentService
// ============================================================
// Cache names:
//   "employees"    → single employee by ID (e.g., employees::12)
//   "allEmployees" → list results with filters
// ============================================================

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // CREATE — evict all list caches
    @CacheEvict(value = "allEmployees", allEntries = true)
    public Employee createEmployee(EmployeeRequestDTO requestDTO) {
        Employee employee = new Employee(
                requestDTO.getName(),
                requestDTO.getDepartment(),
                requestDTO.getDesignation(),
                requestDTO.getSalary()
        );
        return employeeRepository.save(employee);
    }

    // GET ALL — no cache here, caching is done at the controller level
    // (because Page<Employee> can't be deserialized from Redis JSON)
    public Page<Employee> getAllEmployees(String name, String department, String designation, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);

        if (name != null) {
            return employeeRepository.findByNameContainingIgnoreCase(name, pageable);
        } else if (department != null && designation != null) {
            return employeeRepository.findByDepartmentAndDesignation(department, designation, pageable);
        } else if (department != null) {
            return employeeRepository.findByDepartment(department, pageable);
        } else if (designation != null) {
            return employeeRepository.findByDesignation(designation, pageable);
        } else {
            return employeeRepository.findAll(pageable);
        }
    }

    // GET BY ID — cache individual employee, don't cache when not found
    @Cacheable(value = "employees", key = "#id", unless = "#result == null || !#result.isPresent()")
    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    // UPDATE — evict specific + all lists
    @Caching(evict = {
        @CacheEvict(value = "employees", key = "#id"),
        @CacheEvict(value = "allEmployees", allEntries = true)
    })
    public Optional<Employee> updateEmployee(Long id, EmployeeRequestDTO requestDTO) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isPresent()) {
            Employee existing = employee.get();
            existing.setName(requestDTO.getName());
            existing.setDepartment(requestDTO.getDepartment());
            existing.setDesignation(requestDTO.getDesignation());
            existing.setSalary(requestDTO.getSalary());
            return Optional.of(employeeRepository.save(existing));
        }
        return Optional.empty();
    }

    // DELETE — evict specific + all lists
    @Caching(evict = {
        @CacheEvict(value = "employees", key = "#id"),
        @CacheEvict(value = "allEmployees", allEntries = true)
    })
    public Optional<Employee> deleteEmployee(Long id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isPresent()) {
            employeeRepository.deleteById(id);
        }
        return employee;
    }
}
