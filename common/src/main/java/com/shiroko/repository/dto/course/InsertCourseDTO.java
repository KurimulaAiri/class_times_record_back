package com.shiroko.repository.dto.course;

import lombok.Data;

/**
 * Description: 新增课程DTO类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/25 下午3:28
 */
@Data
public class InsertCourseDTO {
    private String courseName;
    private Long courseType;
    private Boolean isAvailable;
    private Long institutionId;
}
