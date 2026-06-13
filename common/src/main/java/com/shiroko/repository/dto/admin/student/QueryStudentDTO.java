package com.shiroko.repository.dto.admin.student;

import com.shiroko.repository.dto.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class QueryStudentDTO extends BaseDTO {
    private Long institutionId;
    private String keyword;
    private Integer sex;
    private Integer currentPage;
    private Integer pageSize;
}
