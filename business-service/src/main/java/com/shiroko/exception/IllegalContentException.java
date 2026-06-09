package com.shiroko.exception;

/**
 * Description: 非法内容异常（参数包含敏感词/违规字符等）
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/2/8 上午1:10
 */
public class IllegalContentException extends RuntimeException {
    // 异常提示信息
    private final String message;

    public IllegalContentException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}