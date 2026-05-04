package com.learning.api.service;

import com.learning.api.entity.Employee;
import com.learning.api.repository.EmployeeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

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

    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    public Optional<Employee> updateEmployee(Long id, Employee updatedEmployee) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isPresent()) {
            Employee existing = employee.get();
            existing.setName(updatedEmployee.getName());
            existing.setDepartment(updatedEmployee.getDepartment());
            existing.setDesignation(updatedEmployee.getDesignation());
            existing.setSalary(updatedEmployee.getSalary());
            return Optional.of(employeeRepository.save(existing));
        }
        return Optional.empty();
    }

    public Optional<Employee> deleteEmployee(Long id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isPresent()) {
            employeeRepository.deleteById(id);
        }
        return employee;
    }
}
