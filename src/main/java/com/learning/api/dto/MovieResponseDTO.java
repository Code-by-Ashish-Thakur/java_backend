package com.learning.api.dto;

import com.learning.api.entity.Movies;

public class MovieResponseDTO {

    private String message;
    private Movies data;

    public MovieResponseDTO() {
    }

    public MovieResponseDTO(String message, Movies data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Movies getData() {
        return data;
    }

    public void setData(Movies data) {
        this.data = data;
    }
}
