package com.shiroko.repository.dto.teacher;

import com.shiroko.repository.dto.teacher.validategroup.QueryGroup;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Description: 查询教师DTO类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/7 下午6:30
 */
@Data
public class QueryTeacherDTO {
    @NotNull(message = "教师ID不能为空", groups = QueryGroup.ByTeacherId.class)
    private Long teacherId;

    @NotNull(message = "机构ID不能为空", groups = QueryGroup.ByInstitutionId.class)
    private Long institutionId;

    @NotNull(message = "当前页不能为空", groups = QueryGroup.ByInstitutionId.class)
    private Long currentPage;

    @NotNull(message = "每页数量不能为空", groups = QueryGroup.ByInstitutionId.class)
    private Long pageSize;
}
