package com.shiroko.aspect;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.shiroko.annotation.UpdateStudentCount;
import com.shiroko.mapper.ClassMapper;
import com.shiroko.mapper.ClassStudentMapper;
import com.shiroko.repository.entity.Class;
import com.shiroko.repository.entity.ClassStudent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * Description: TODO
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/7 下午8:19
 */
@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class StudentCountAspect {

    private final ClassMapper classMapper;
    // 关键：这里需要注入中间表的 Mapper
    private final ClassStudentMapper classStudentMapper;

    @AfterReturning(pointcut = "@annotation(updateStudentCount)", returning = "result")
    public void handleStudentCount(JoinPoint joinPoint, UpdateStudentCount updateStudentCount, Object result) {
        Object[] args = joinPoint.getArgs();
        Long classId = extractClassId(args);

        if (classId != null) {
            log.info("触发 AOP 更新班级人数，班级ID: {}", classId);

            // 2. 重写：从中间表（class_student）统计人数
            // 假设中间表对应的实体类是 ClassStudent
            Long currentCount = classStudentMapper.selectCount(
                    new LambdaQueryWrapper<ClassStudent>()
                            .eq(ClassStudent::getClassId, classId)
            );

            // 3. 更新到 Class 表（实体类名建议改为 Clazz 避免关键字冲突）
            classMapper.update(null, new LambdaUpdateWrapper<Class>()
                    .set(Class::getStudentCount, currentCount.intValue())
                    .eq(Class::getId, classId));

            log.info("班级ID: {} 人数已更新为: {}", classId, currentCount);
        }
    }

    private Long extractClassId(Object[] args) {
        if (args == null || args.length == 0) return null;

        // 策略1：如果第一个参数直接是 Long 型的 classId
        if (args[0] instanceof Long) {
            return (Long) args[0];
        }

        // 策略2：如果参数是一个 DTO 对象，尝试反射获取 classId 字段
        // 这里可以根据你具体的 Service 方法参数来调整
        try {
            Object dto = args[0];
            Field field = dto.getClass().getDeclaredField("classId");
            field.setAccessible(true);
            return (Long) field.get(dto);
        } catch (Exception e) {
            log.warn("AOP 提取 classId 失败: {}", e.getMessage());
            return null;
        }
    }
}