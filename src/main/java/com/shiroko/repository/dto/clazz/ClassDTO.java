package com.shiroko.repository.dto.clazz;

import com.shiroko.repository.entity.CourseRecord;
import com.shiroko.repository.entity.Teacher;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

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

    private List<Teacher> teachers;

    private CourseRecord courseRecord;

    private Long courseType;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
