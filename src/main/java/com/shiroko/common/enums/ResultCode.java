package com.shiroko.common.enums;

import lombok.Getter;

/**
 * Description: 状态码枚举类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/15 下午7:25
 */
@Getter
public enum ResultCode {
    SUCCESS(200, "操作成功"),
    PARAM_ERROR(400, "参数错误"),
    COURSE_BALANCE_NOT_ENOUGH(1001, "课程余额不足"),
    STUDENT_ALREADY_IN_CLASS(1002, "学生已在班级中"),
    SYSTEM_ERROR(500, "服务器开小差了");

    private final int code;
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}

