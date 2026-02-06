package com.shiroko.repository.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    public static <T> ResponseDTO<T> success(T data) {
        return new ResponseDTO<>(200L, "success", data);
    }

    public static <T> ResponseDTO<T> fail(String message) {
        return new ResponseDTO<>(400L, message, null);
    }

    public static <T> ResponseDTO<T> fail(String message, T data) {
        return new ResponseDTO<>(400L, message, data);
    }
}
