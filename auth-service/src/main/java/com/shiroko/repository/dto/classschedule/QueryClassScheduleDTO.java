package com.shiroko.repository.dto.classschedule;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description: 班级排课查询参数
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/16 下午10:28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryClassScheduleDTO {
    private Long id;

    private Long institutionId;

    private Long classId;

    private Long currentPage;

    private Long pageSize;
}
