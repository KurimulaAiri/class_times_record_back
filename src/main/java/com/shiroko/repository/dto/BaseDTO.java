package com.shiroko.repository.dto;

/**
 * Description: 基础 DTO 类，包含通用的字段和方法
 * 有必要字段一定要加校验注解
 * 校验注解：
 * '@NotNull'：不能为空，用于对象类型
 * '@NotEmpty'：不能为空且非空格，用于字符串类型
 * '@NotBlank'：不能为空且非空格，用于字符串类型，与 '@NotEmpty' 相同，只是 '@NotBlank' 会校验字符串且非空格
 * '@Email'：邮箱格式
 * '@Phone'：手机号格式
 *
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/2/7 上午1:19
 */
public class BaseDTO {
}
