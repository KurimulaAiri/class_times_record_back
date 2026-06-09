package com.shiroko.repository.dto.clazz;

import com.shiroko.repository.entity.ClassSchedule;
import com.shiroko.repository.entity.Teacher;
import lombok.Data;

import java.util.List;

/**
 * Description: 班级插入DTO类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/17 上午10:51
 */
@Data
public class InsertClassDTO {

    private String className;

    private Long courseId;

    private Long maxCount;

    private List<ClassSchedule> schedules;

    private List<Teacher> teachers;

}
