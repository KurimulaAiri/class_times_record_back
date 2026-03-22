package com.shiroko.repository.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Description: 记录VO
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/3/22 下午11:01
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecordVO extends BaseVO {
    private Long id;
    private Long courseRecordId;
    private LocalDateTime recordTime;
    private Long recordType;
    private String recordRemark;
    private Long recordChange;
    private String updateTimeStr;
    private String createTimeStr;
}
