package com.shiroko.repository.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Description: 插入课程记录DTO
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/3/22 下午10:57
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InsertRecordDTO extends BaseDTO {
    @NotBlank(message = "课程记录ID不能为空")
    private Long courseRecordId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @NotBlank(message = "记录时间不能为空")
    private LocalDateTime recordTime;
    @NotBlank(message = "记录类型不能为空")
    private Long recordType;
    private String recordRemark;
    @NotBlank(message = "记录变更不能为空")
    private Long recordChange;
}
