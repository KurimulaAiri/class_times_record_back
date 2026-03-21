package com.shiroko.repository.dto;

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
    @NotBlank(message = "学生姓名不能为空")
    private String stuName;
    @NotBlank(message = "课程名称不能为空")
    private String courseName;
    @NotNull(message = "课程总时间不能为空")
    private Long courseTotalTime;
    @NotNull(message = "课程剩余时间不能为空")
    private Long courseRestTime;
    private String courseRemark;
}
