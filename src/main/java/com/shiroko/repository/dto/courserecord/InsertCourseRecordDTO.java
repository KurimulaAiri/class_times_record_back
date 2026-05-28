package com.shiroko.repository.dto.courserecord;

import com.shiroko.repository.dto.BaseDTO;
import com.shiroko.repository.dto.courserecord.validategroup.InsertGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Description: TODO
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/3/21 下午1:50
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InsertCourseRecordDTO extends BaseDTO {
    @NotBlank(message = "学生姓名不能为空", groups = InsertGroup.OldGroup.class)
    private String stuName;

    @NotNull(message = "学生ID不能为空", groups = InsertGroup.NewGroup.class)
    private Long studentId;

    @NotBlank(message = "课程名称不能为空", groups = InsertGroup.OldGroup.class)
    private String courseName;

    @NotNull(message = "课程ID不能为空", groups = InsertGroup.NewGroup.class)
    private Long courseId;

    @NotNull(message = "课程总时间不能为空", groups = {InsertGroup.OldGroup.class, InsertGroup.NewGroup.class})
    private Long courseTotalTime;

    @NotNull(message = "课程剩余时间不能为空", groups = {InsertGroup.OldGroup.class, InsertGroup.NewGroup.class})
    private Long courseRestTime;

    private String courseRemark;
}
