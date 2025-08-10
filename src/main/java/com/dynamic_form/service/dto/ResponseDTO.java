package com.dynamic_form.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDTO<T> {
    private boolean success;
    private String message;
    private T data;
    private long timestamp;

    public static <T> ResponseDTO<T> success(T data, String message) {
        return new ResponseDTO<>(
                true,
                message,
                data,
                System.currentTimeMillis()
        );
    }

    public static <T> ResponseDTO<T> error(String message) {
        return new ResponseDTO<>(
                false,
                message,
                null,
                System.currentTimeMillis()
        );
    }
}
