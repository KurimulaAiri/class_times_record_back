package com.shiroko.repository.dto.admin.course;

import lombok.Data;

@Data
public class AdminUpdateCourseDTO {
    private Long id;
    private String courseName;
    private Long courseType;
    private Integer isAvailable;
}
