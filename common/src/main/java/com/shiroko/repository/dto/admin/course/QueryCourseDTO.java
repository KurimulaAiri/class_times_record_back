package com.shiroko.repository.dto.admin.course;

import com.shiroko.repository.dto.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class QueryCourseDTO extends BaseDTO {
    private Long institutionId;
    private String keyword;
    private Integer courseType;
    private Integer isAvailable;
    private Integer currentPage;
    private Integer pageSize;
}
