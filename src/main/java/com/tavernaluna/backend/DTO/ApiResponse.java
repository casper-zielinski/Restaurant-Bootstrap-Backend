package com.tavernaluna.backend.DTO;

import java.util.Optional;

public record ApiResponse<T>(boolean success, String message, T data, Optional<String> errorMessage) {
    public static <T> ApiResponse<T> ok(String message, T data) {
        return new ApiResponse<>(true, message, data, null);
    }

    public static <T> ApiResponse<T> error(String message, T data) {
        return new ApiResponse<>(false, message, data, null);
    }

    public static <T> ApiResponse<T> error(String message, T data, String errorMessage) {
        return new ApiResponse<>(false, message, data, Optional.of(errorMessage));
    }
}
