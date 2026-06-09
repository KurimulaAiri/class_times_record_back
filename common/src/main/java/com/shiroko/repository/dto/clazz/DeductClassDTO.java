package com.shiroko.repository.dto.clazz;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Description: 通过班级扣课DTO类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/15 下午6:50
 */
@Data
@AllArgsConstructor
public class DeductClassDTO {
    private Long classId;
    private Long courseId;
    private Integer deductCount;
    private String remark;
}
