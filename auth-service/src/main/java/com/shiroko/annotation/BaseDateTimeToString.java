package com.shiroko.annotation;

import org.mapstruct.Qualifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Description: 基础日期时间转换注解
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/6/6 上午12:27
 */
@Qualifier // 必须加这个，标记为 MapStruct 限定符
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface BaseDateTimeToString {
}
