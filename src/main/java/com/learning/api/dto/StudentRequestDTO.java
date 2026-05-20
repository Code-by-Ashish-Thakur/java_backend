package com.learning.api.dto;

public class StudentRequestDTO {

    private String name;
    private int rollNo;
    private String stream;
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
