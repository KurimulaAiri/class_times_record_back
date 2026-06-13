package com.shiroko.repository.dto.admin.clazz;

import com.shiroko.repository.dto.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class QueryClassDTO extends BaseDTO {
    private Long institutionId;
    private Long courseId;
    private String keyword;
    private Integer status;
    private Integer currentPage;
    private Integer pageSize;
}
