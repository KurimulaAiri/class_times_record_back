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

    private String stuName;

    private String courseName;

    private String courseRemark;

    private Long courseStatus;

    private Long currentPage;

    private Long pageSize;
}
