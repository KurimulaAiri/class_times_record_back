package com.shiroko.repository.dto.admin.institution;

import com.shiroko.repository.dto.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class QueryInstitutionDTO extends BaseDTO {
    private Long institutionId;
    private String institutionName;
    private Integer status;
    private Integer currentPage;
    private Integer pageSize;
}
