package com.learning.api.controller;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import com.learning.api.entity.Employee;
import com.learning.api.service.EmployeeService;

@RestController
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/api/employees")
    public Map<String, Object> createEmployee(@RequestBody Employee employee) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Employee saved successfully");
        response.put("data", employeeService.createEmployee(employee));
        return response;
    }

    @GetMapping("/api/employees")
    public Map<String, Object> getAllEmployees(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String designation,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Map<String, Object> response = new LinkedHashMap<>();
        Page<Employee> employeePage = employeeService.getAllEmployees(name, department, designation, page, size);

        response.put("message", "Found " + employeePage.getTotalElements() + " employee(s)");
        response.put("data", employeePage.getContent());
        response.put("currentPage", employeePage.getNumber());
        response.put("totalPages", employeePage.getTotalPages());
        response.put("totalItems", employeePage.getTotalElements());
        return response;
    }

    @GetMapping("/api/employees/{id}")
    public Map<String, Object> getEmployeeById(@PathVariable Long id) {
        Map<String, Object> response = new LinkedHashMap<>();
        Optional<Employee> employee = employeeService.getEmployeeById(id);
        if (employee.isPresent()) {
            response.put("message", "Employee found successfully");
            response.put("data", employee.get());
        } else {
            response.put("message", "Employee not found with id: " + id);
            response.put("data", null);
        }
        return response;
    }

    @PutMapping("/api/employees/{id}")
    public Map<String, Object> updateEmployee(@PathVariable Long id, @RequestBody Employee updatedEmployee) {
        Map<String, Object> response = new LinkedHashMap<>();
        Optional<Employee> employee = employeeService.updateEmployee(id, updatedEmployee);
        if (employee.isPresent()) {
            response.put("message", "Employee updated successfully");
            response.put("data", employee.get());
        } else {
            response.put("message", "Employee not found with id: " + id);
            response.put("data", null);
        }
        return response;
    }

    @DeleteMapping("/api/employees/{id}")
    public Map<String, Object> deleteEmployee(@PathVariable Long id) {
        Map<String, Object> response = new LinkedHashMap<>();
        Optional<Employee> employee = employeeService.deleteEmployee(id);
        if (employee.isPresent()) {
            response.put("message", "Employee deleted successfully");
            response.put("data", employee.get());
        } else {
            response.put("message", "Employee not found with id: " + id);
            response.put("data", null);
        }
        return response;
    }
}
