package com.shiroko.repository.dto.admin.teacher;

import com.shiroko.repository.dto.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class QueryTeacherDTO extends BaseDTO {
    private Long institutionId;
    private String keyword;
    private Integer isAvailable;
    private Integer currentPage;
    private Integer pageSize;
}
