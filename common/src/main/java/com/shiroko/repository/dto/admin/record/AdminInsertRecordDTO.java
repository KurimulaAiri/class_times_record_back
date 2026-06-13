package com.shiroko.repository.dto.admin.record;

import lombok.Data;

@Data
public class AdminInsertRecordDTO {
    private Long courseRecordId;
    private Long recordType;
    private Long recordChange;
    private String recordTime;
    private String recordRemark;
}
