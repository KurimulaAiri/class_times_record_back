package com.shiroko.repository.dto.student;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Description: 更新学生信息DTO
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/4/29 下午6:31
 */
@Data
public class UpdateStudentDTO {

    private Long id;

    private LocalDateTime birth;

    private String school;

    private String address;

}
