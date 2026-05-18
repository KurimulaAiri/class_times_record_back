package com.shiroko.repository.dto.clazz;

import com.shiroko.repository.dto.clazz.validategroup.UpdateGroup;
import com.shiroko.repository.dto.student.StudentDTO;
import com.shiroko.repository.entity.ClassSchedule;
import com.shiroko.repository.entity.Teacher;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * Description: 更新班级信息DTO
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/15 上午1:39
 */
@Data
public class UpdateClassDTO {

    @NotNull(message = "班级ID不能为空", groups = {UpdateGroup.RemoveStudent.class, UpdateGroup.UpdateClass.class})
    private Long classId;

    private String className;

    private Long courseId;

    private List<Teacher> teachers;

    private List<ClassSchedule> schedules;

    @NotEmpty(message = "学生列表不能为空", groups = {UpdateGroup.RemoveStudent.class})
    private List<StudentDTO> students;

}
