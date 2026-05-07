package com.shiroko.repository.dto.teacher;

import lombok.Data;

/**
 * Description: 查询教师DTO类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/7 下午6:30
 */
@Data
public class QueryTeacherDTO {

    private Long teacherId;

    private Long institutionId;
}
