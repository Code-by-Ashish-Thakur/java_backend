package com.learning.api.controller;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import com.learning.api.dto.EmployeeRequestDTO;
import com.learning.api.dto.EmployeeResponseDTO;
import com.learning.api.dto.EmployeePageResponseDTO;
import com.learning.api.entity.Employee;
import com.learning.api.service.EmployeeService;

@RestController
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/api/employees")
    public EmployeeResponseDTO createEmployee(@RequestBody EmployeeRequestDTO requestDTO) {
        return new EmployeeResponseDTO("Employee saved successfully", employeeService.createEmployee(requestDTO));
    }

    @GetMapping("/api/employees")
    public EmployeePageResponseDTO getAllEmployees(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String designation,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Employee> employeePage = employeeService.getAllEmployees(name, department, designation, page, size);

        return new EmployeePageResponseDTO(
                "Found " + employeePage.getTotalElements() + " employee(s)",
                employeePage.getContent(),
                employeePage.getNumber(),
                employeePage.getTotalPages(),
                employeePage.getTotalElements()
        );
    }

    @GetMapping("/api/employees/{id}")
    public EmployeeResponseDTO getEmployeeById(@PathVariable Long id) {
        Optional<Employee> employee = employeeService.getEmployeeById(id);
        if (employee.isPresent()) {
            return new EmployeeResponseDTO("Employee found successfully", employee.get());
        }
        return new EmployeeResponseDTO("Employee not found with id: " + id, null);
    }

    @PutMapping("/api/employees/{id}")
    public EmployeeResponseDTO updateEmployee(@PathVariable Long id, @RequestBody EmployeeRequestDTO requestDTO) {
        Optional<Employee> employee = employeeService.updateEmployee(id, requestDTO);
        if (employee.isPresent()) {
            return new EmployeeResponseDTO("Employee updated successfully", employee.get());
        }
        return new EmployeeResponseDTO("Employee not found with id: " + id, null);
    }

    @DeleteMapping("/api/employees/{id}")
    public EmployeeResponseDTO deleteEmployee(@PathVariable Long id) {
        Optional<Employee> employee = employeeService.deleteEmployee(id);
        if (employee.isPresent()) {
            return new EmployeeResponseDTO("Employee deleted successfully", employee.get());
        }
        return new EmployeeResponseDTO("Employee not found with id: " + id, null);
    }
}
