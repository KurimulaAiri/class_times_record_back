package com.shiroko.repository.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Description: 插入记录DTO
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/3/22 下午11:09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class InsertRecordsDTO extends BaseDTO {
    // 1. 数组/集合校验请用 @NotEmpty 或 @NotNull
    @NotEmpty(message = "课程记录ID不能为空")
    private Long[] courseRecordIdList;

    // 2. 数字类型（Long）校验请用 @NotNull
    @NotNull(message = "记录类型不能为空")
    private Long recordType;

    private String recordRemark;

    // 3. 日期类型校验用 @NotNull
    // 4. 如果前端只传 "2026-03-22"，建议改为 LocalDate
    //    如果必须用 LocalDateTime，前端传值必须补齐 " 00:00:00"
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @NotNull(message = "记录时间不能为空")
    private LocalDateTime recordTime; // 强烈建议改为 LocalDate

    @NotNull(message = "记录变更不能为空")
    private Long recordChange;
}
