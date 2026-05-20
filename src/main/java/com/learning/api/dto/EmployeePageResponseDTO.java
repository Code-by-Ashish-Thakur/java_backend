package com.learning.api.dto;

import java.util.List;

import com.learning.api.entity.Employee;

public class EmployeePageResponseDTO {

    private String message;
    private List<Employee> data;
    private int currentPage;
    private int totalPages;
    private long totalItems;

    public EmployeePageResponseDTO() {
    }

    public EmployeePageResponseDTO(String message, List<Employee> data, int currentPage, int totalPages, long totalItems) {
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

    public List<Employee> getData() {
        return data;
    }

    public void setData(List<Employee> data) {
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
