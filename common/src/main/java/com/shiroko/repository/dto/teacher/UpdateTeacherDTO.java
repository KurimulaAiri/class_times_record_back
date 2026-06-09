package com.shiroko.repository.dto.teacher;

import lombok.Data;

/**
 * Description: 更新教师DTO
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/6/7 下午1:17
 */
@Data
public class UpdateTeacherDTO {
    private Long institutionId;
    private Long teacherId;
    private String username;
    private Boolean isAvailable;
    private String account;
    private String password;
}
