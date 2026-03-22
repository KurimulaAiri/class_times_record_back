package com.shiroko.repository.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Description: 更新课程记录DTO
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/3/21 下午8:19
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCourseRecordDTO extends BaseDTO {
    private Long id;
    private Long courseTotalTime;
    private Long courseRestTime;
    private Long courseStatus;
    private String courseName;
    private String courseRemark;
    private String stuName;
   }
