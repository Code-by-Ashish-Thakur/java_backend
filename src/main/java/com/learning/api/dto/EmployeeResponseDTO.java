package com.learning.api.dto;

import com.learning.api.entity.Employee;

public class EmployeeResponseDTO {

    private String message;
    private Employee data;

    public EmployeeResponseDTO() {
    }

    public EmployeeResponseDTO(String message, Employee data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Employee getData() {
        return data;
    }

    public void setData(Employee data) {
        this.data = data;
    }
}
