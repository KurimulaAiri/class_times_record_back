package com.shiroko.exception;

import com.shiroko.common.enums.ResultCode;
import lombok.Getter;

/**
 * Description: 业务异常类，用于处理业务逻辑中的异常
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/15 下午7:21
 */
@Getter
public class BusinessException extends RuntimeException {

    private final Integer code;

    /**
     * 默认 500 错误的构造
     */
    public BusinessException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
    }

    /**
     * 自定义状态码和信息的构造
     */
// 支持动态拼串，比如 "学生[张三]余额不足"
    public BusinessException(ResultCode resultCode, String dynamicMsg) {
        super(dynamicMsg);
        this.code = resultCode.getCode();
    }
}