package com.learning.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class StudentRequestDTO {

    @NotBlank(message = "Name is required")
    private String name;

    @Min(value = 1, message = "Roll number is required and must be greater than 0")
    private int rollNo;

    @NotBlank(message = "Stream is required")
    private String stream;

    @NotBlank(message = "Student class is required")
    private String studentClass;

    public StudentRequestDTO() {
    }

    public StudentRequestDTO(String name, int rollNo, String stream, String studentClass) {
        this.name = name;
        this.rollNo = rollNo;
        this.stream = stream;
        this.studentClass = studentClass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRollNo() {
        return rollNo;
    }

    public void setRollNo(int rollNo) {
        this.rollNo = rollNo;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    public String getStudentClass() {
        return studentClass;
    }

    public void setStudentClass(String studentClass) {
        this.studentClass = studentClass;
    }
}
