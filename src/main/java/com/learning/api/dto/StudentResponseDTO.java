package com.learning.api.dto;

import com.learning.api.entity.Student;

public class StudentResponseDTO {

    private String message;
    private Student data;

    public StudentResponseDTO() {
    }

    public StudentResponseDTO(String message, Student data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Student getData() {
        return data;
    }

    public void setData(Student data) {
        this.data = data;
    }
}
