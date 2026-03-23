package com.shiroko.repository.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Description: 查询课程记录DTO
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/3/23 上午11:41
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class QueryRecordDTO extends BaseDTO {
    private Long courseRecordId;
    private Integer pageSize;
    private Integer currentPage;
}
