package com.learning.api.dto;

import java.io.Serializable;
import java.util.List;

import com.learning.api.entity.Student;

public class StudentPageResponseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String message;
    private List<Student> data;
    private int currentPage;
    private int totalPages;
    private long totalItems;

    public StudentPageResponseDTO() {
    }

    public StudentPageResponseDTO(String message, List<Student> data, int currentPage, int totalPages, long totalItems) {
        this.message = message;
        this.data = data;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalItems = totalItems;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Student> getData() {
        return data;
    }

    public void setData(List<Student> data) {
        this.data = data;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(long totalItems) {
        this.totalItems = totalItems;
    }
}
