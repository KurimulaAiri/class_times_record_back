package com.shiroko.repository.vo.admin.record;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminRecordVO {
    private Long id;
    private Long courseRecordId;
    private String recordTimeStr;
    private String recordRemark;
    private Long recordType;
    private Long recordChange;
    private Long operateTeacherId;
    private String createTimeStr;
    private String updateTimeStr;
}
