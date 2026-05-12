package com.shiroko.repository.dto.student;

import com.shiroko.repository.dto.student.validategroup.QueryGroup;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description: 查询学生DTO类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/4/24 上午1:10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryStudentDTO {
    @NotNull(message = "学生ID不能为空", groups = QueryGroup.ByStudentId.class)
    private Long studentId;

    @NotNull(message = "班级ID不能为空", groups = QueryGroup.ByClassId.class)
    private Long classId;

    @NotNull(message = "家长ID不能为空", groups = QueryGroup.ByParentId.class)
    private Long parentId;

    @NotNull(message = "机构ID不能为空", groups = QueryGroup.ByInstitutionId.class)
    private Long institutionId;

    @NotNull(message = "教师ID不能为空", groups = QueryGroup.ByTeacherId.class)
    private Long teacherId;

    private Long sex;

    private String keyword;

    @NotNull(message = "每页数量不能为空", groups = {
            QueryGroup.ByParentId.class,
            QueryGroup.ByTeacherId.class,
            QueryGroup.ByClassId.class,
            QueryGroup.ByInstitutionId.class
    })
    private Long pageSize;

    @NotNull(message = "当前页不能为空", groups = {
            QueryGroup.ByParentId.class,
            QueryGroup.ByTeacherId.class,
            QueryGroup.ByClassId.class,
            QueryGroup.ByInstitutionId.class
    })
    private Long currentPage;

}
