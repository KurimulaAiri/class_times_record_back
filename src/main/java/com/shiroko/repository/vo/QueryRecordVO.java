package com.shiroko.repository.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Description: 查询课程记录VO
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/3/23 上午11:45
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class QueryRecordVO {

    private List<RecordVO> records;

    private Long total;

}
