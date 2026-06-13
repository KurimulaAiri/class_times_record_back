package com.shiroko.repository.dto.admin.course;

import lombok.Data;

@Data
public class AdminInsertCourseDTO {
    private String courseName;
    private Long courseType;
    private Long institutionId;
    private Integer isAvailable;
}
