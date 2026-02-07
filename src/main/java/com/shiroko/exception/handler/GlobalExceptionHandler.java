package com.shiroko.exception.handler;

import com.shiroko.repository.dto.ResponseDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * 全局异常处理类
 * 修复：用ResponseEntity包装返回值，明确返回JSON，避免视图转发
 * @author Guguguy
 * @version 1.0
 * @since 2026/2/8 上午1:05
 */
@RestControllerAdvice // 该注解已包含@ResponseBody，确保返回JSON
public class GlobalExceptionHandler {

    // 处理参数校验异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDTO<Void>> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        String errorMsg = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("；"));
        // 用ResponseEntity包装，明确返回JSON+HTTP状态码
        return new ResponseEntity<>(ResponseDTO.fail("参数校验失败：" + errorMsg), HttpStatus.BAD_REQUEST);
    }

    // 处理SQL错误/运行时异常（核心修复）
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseDTO<Void>> handleRuntimeException(RuntimeException e) {
        e.printStackTrace(); // 打印异常栈，方便排查SQL错误
        // 返回500状态码+JSON，而非视图
        return new ResponseEntity<>(ResponseDTO.fail("系统异常：" + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 其他异常处理方法同理，都用ResponseEntity包装
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponseDTO<Void>> handleConstraintViolation(ConstraintViolationException e) {
        String errorMsg = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("；"));
        return new ResponseEntity<>(ResponseDTO.fail("参数校验失败：" + errorMsg), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ResponseDTO<Void>> handleBindException(BindException e) {
        String errorMsg = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("；"));
        return new ResponseEntity<>(ResponseDTO.fail("参数校验失败：" + errorMsg), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ResponseDTO<Void>> handleMediaTypeError(HttpMediaTypeNotSupportedException e) {
        return new ResponseEntity<>(ResponseDTO.fail("请求格式错误：仅支持application/json"), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }
}