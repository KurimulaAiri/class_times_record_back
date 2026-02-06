package com.shiroko.repository.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description: TODO
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/2/7 上午1:19
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class QueryCourseRecordDTO {
    private Long userId;
    private Long courseId;
     private Long currentPage;
     private Long pageSize;
}
