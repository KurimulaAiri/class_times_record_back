package com.shiroko.exception.handler;

import com.shiroko.exception.BusinessException;
import com.shiroko.repository.dto.ResponseDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * 全局异常处理类
 * 修复：用ResponseEntity包装返回值，明确返回JSON，避免视图转发
 * @author Guguguy
 * @version 1.0
 * @since 2026/2/8 上午1:05
 */
@RestControllerAdvice // 该注解已包含@ResponseBody，确保返回 JSON
public class GlobalExceptionHandler {

    private final Logger logger;

    public GlobalExceptionHandler() {
        this.logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    }

    // 处理参数校验异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDTO<Void>> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        String errorMsg = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(";"));
        // 用ResponseEntity包装，明确返回JSON+HTTP状态码
        return new ResponseEntity<>(ResponseDTO.fail("参数校验失败：" + errorMsg), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ResponseDTO<Void>> handleBusinessException(BusinessException e) {
        // 1. 构造响应体，包含你自定义的业务码（如 1001）和错误消息
        ResponseDTO<Void> body = ResponseDTO.fail(Long.valueOf(e.getCode()), e.getMessage());

        // 2. 固定返回 HTTP 状态码 200 (OK) 或者 500 (Internal Server Error)
        // 这样前端就能正常解析到 JSON 内容，而不是被浏览器/框架的 500 拦截
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    // ==================== 🌟 新增：精准捕获联合索引冲突 🌟 ====================
    @ExceptionHandler(org.springframework.dao.DuplicateKeyException.class)
    public ResponseEntity<ResponseDTO<Void>> handleDuplicateKeyException(org.springframework.dao.DuplicateKeyException e) {
        logger.error("数据重复冲突异常: ", e);

        String rootMsg = e.getRootCause() != null ? e.getRootCause().getMessage() : "";
        String userFriendlyMsg = "该记录已存在，请勿重复提交";

        // 针对你的 course_record 表做定制化友好提示
        if (rootMsg.contains("course_record")) {
            userFriendlyMsg = "该学生已绑定过此课程，请勿重复添加";
        }

        // 返回 400 或者是自定义的 409 (Conflict) 状态码，配合你的统一 DTO
        return new ResponseEntity<>(ResponseDTO.fail(409L, userFriendlyMsg), HttpStatus.CONFLICT);
    }

    // ==================== 🌟 新增：精准捕获外键约束失败 🌟 ====================
    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public ResponseEntity<ResponseDTO<Void>> handleDataIntegrityViolationException(org.springframework.dao.DataIntegrityViolationException e) {
        logger.error("数据完整性/外键约束冲突异常: ", e);

        String rootMsg = e.getRootCause() != null ? e.getRootCause().getMessage() : "";
        String userFriendlyMsg = "数据关联校验失败，引用的上级数据不存在";

        if (rootMsg.contains("foreign key constraint fails")) {
            userFriendlyMsg = "关联引用的上级数据（如学生或课程）不存在，请刷新重试";
        }

        return new ResponseEntity<>(ResponseDTO.fail(400L, userFriendlyMsg), HttpStatus.BAD_REQUEST);
    }

    // 处理SQL错误/运行时异常（核心修复）
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseDTO<Void>> handleRuntimeException(RuntimeException e) {
        logger.error("系统运行时出现未捕获异常: ", e); // 直接把整个异常对象 e 作为最后一个参数传进去，SLF4J 会自动展开打印完整的报错行数和堆栈
        // 返回500状态码+JSON，而非视图
        return new ResponseEntity<>(ResponseDTO.fail(500L, "系统异常：" + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 其他异常处理方法同理，都用ResponseEntity包装
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponseDTO<Void>> handleConstraintViolation(ConstraintViolationException e) {
        String errorMsg = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(";"));
        return new ResponseEntity<>(ResponseDTO.fail(400L, "参数校验失败：" + errorMsg), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ResponseDTO<Void>> handleBindException(BindException e) {
        String errorMsg = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(";"));
        return new ResponseEntity<>(ResponseDTO.fail("参数校验失败：" + errorMsg), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ResponseDTO<Void>> handleMediaTypeError(HttpMediaTypeNotSupportedException e) {
        return new ResponseEntity<>(ResponseDTO.fail("请求格式错误：仅支持application/json" + e.getSupportedMediaTypes()), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }


}