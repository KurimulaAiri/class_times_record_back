package com.shiroko.repository.dto.admin.courserecord;

import com.shiroko.repository.dto.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class QueryCourseRecordDTO extends BaseDTO {
    private Long studentId;
    private Long courseId;
    private Integer courseStatus;
    private Integer currentPage;
    private Integer pageSize;
}
