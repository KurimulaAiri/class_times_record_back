package com.shiroko.repository.dto.teacher;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description: 插入教师DTO类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/6/8 上午12:51
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InsertTeacherDTO {
    private String username;
    private String account;
    private String password;
    private Long institutionId;
}
