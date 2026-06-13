package com.shiroko.repository.dto.admin.teacher;

import lombok.Data;

@Data
public class AdminInsertTeacherDTO {
    private String username;
    private Long institutionId;
    private Integer isAvailable;
}
