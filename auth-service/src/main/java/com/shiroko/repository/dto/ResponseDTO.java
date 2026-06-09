package com.shiroko.repository.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Description: 响应数据传输对象
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

    private final String requestTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

    public static <T> ResponseDTO<T> success(T data) {
        return new ResponseDTO<>(200L, "success", data);
    }

    public static <T> ResponseDTO<T> success(String message, T data) {
        return new ResponseDTO<>(200L, message, data);
    }

    public static <T> ResponseDTO<T> fail(String message) {
        return new ResponseDTO<>(400L, message, null);
    }

    public static <T> ResponseDTO<T> fail(Long code, String message) {
        return new ResponseDTO<>(code, message, null);
    }

    public static <T> ResponseDTO<T> fail(String message, T data) {
        return new ResponseDTO<>(400L, message, data);
    }
}
