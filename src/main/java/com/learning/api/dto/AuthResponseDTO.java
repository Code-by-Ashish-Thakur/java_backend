package com.learning.api.dto;

import com.learning.api.entity.User;

public class AuthResponseDTO {

    private String message;
    private User data;
    private String token;

    public AuthResponseDTO() {
    }

    public AuthResponseDTO(String message, User data) {
        this.message = message;
        this.data = data;
    }

    public AuthResponseDTO(String message, User data, String token) {
        this.message = message;
        this.data = data;
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
