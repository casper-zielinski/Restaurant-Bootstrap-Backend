package com.tavernaluna.backend.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Optional;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(boolean success, String message, T data, String errorMessage) {
    public static <T> ApiResponse<T> ok(String message, T data) {
        return new ApiResponse<>(true, message, data, null);
    }

    public static <T> ApiResponse<T> error(String message, T data) {
        return new ApiResponse<>(false, message, data, null);
    }

    public static <T> ApiResponse<T> error(String message, T data, String errorMessage) {
        return new ApiResponse<>(false, message, data, errorMessage);
    }
}
