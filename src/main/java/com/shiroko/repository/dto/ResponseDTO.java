package com.shiroko.repository.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Description: TODO
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/2/7 上午1:37
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResponseDTO<T> {

    private Long code;

    private String message;

    private T data;

    private String requestTime;

    public static <T> ResponseDTO<T> success(T data) {
        return new ResponseDTO<>(200L, "success", data, LocalDateTime.now().toString());
    }

    public static <T> ResponseDTO<T> fail(String message) {
        return new ResponseDTO<>(400L, message, null, LocalDateTime.now().toString());
    }

    public static <T> ResponseDTO<T> fail(Long code, String message) {
        return new ResponseDTO<>(code, message, null, LocalDateTime.now().toString());
    }

    public static <T> ResponseDTO<T> fail(String message, T data) {
        return new ResponseDTO<>(400L, message, data, LocalDateTime.now().toString());
    }
}
