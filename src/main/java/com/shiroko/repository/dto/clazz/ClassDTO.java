package com.shiroko.repository.dto.clazz;

import com.shiroko.repository.entity.CourseRecord;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Description: 班级DTO类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/7 下午9:18
 */
@Data
public class ClassDTO {

    private Long id;

    private String className;

    private Long studentCount;

    private Long studentMaxCount;

    private Long courseId;

    private String courseName;

    private String username;

    private CourseRecord courseRecord;

    private Long courseType;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
