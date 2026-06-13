package com.shiroko.repository.dto.admin.classschedule;

import com.shiroko.repository.dto.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class QueryClassScheduleDTO extends BaseDTO {
    private Long classId;
    private Integer dayOfWeek;
    private Integer currentPage;
    private Integer pageSize;
}
