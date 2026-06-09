package com.shiroko.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Description: 更新学生人数注解，用于在方法上标识需要更新学生人数
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/7 下午8:19
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UpdateStudentCount {

}