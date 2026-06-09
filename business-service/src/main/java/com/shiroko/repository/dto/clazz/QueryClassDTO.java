package com.shiroko.repository.dto.clazz;

import lombok.Data;

/**
 * Description: 查询班级DTO类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/7 下午9:04
 */
@Data
public class QueryClassDTO {
    private Long classId;
    private Long classStatus;
    private Long studentId;
    private Long teacherId;
    private Long institutionId;
    private String keyword;
}
