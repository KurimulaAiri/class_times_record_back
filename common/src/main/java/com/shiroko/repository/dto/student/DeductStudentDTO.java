package com.shiroko.repository.dto.student;

import lombok.Data;

/**
 * Description: 通过学生扣课DTO类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/22 下午3:10
 */
@Data
public class DeductStudentDTO {
    private Long studentId;
    private Integer deductCount;
    private String remark;
}
