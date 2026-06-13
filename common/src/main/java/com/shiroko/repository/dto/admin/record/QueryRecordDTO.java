package com.shiroko.repository.dto.admin.record;

import com.shiroko.repository.dto.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class QueryRecordDTO extends BaseDTO {
    private Long courseRecordId;
    private Integer recordType;
    private Integer currentPage;
    private Integer pageSize;
}
