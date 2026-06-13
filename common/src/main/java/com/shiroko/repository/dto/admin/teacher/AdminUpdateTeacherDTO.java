package com.shiroko.repository.dto.admin.teacher;

import lombok.Data;

@Data
public class AdminUpdateTeacherDTO {
    private Long id;
    private String username;
    private Integer isAvailable;
}
