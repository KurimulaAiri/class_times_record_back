package com.shiroko.repository.dto.courserecord;

import com.shiroko.repository.dto.courserecord.validategroup.QueryGroup;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description: TODO
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/2/7 上午1:19
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class QueryCourseRecordDTO {

    private Boolean share;

    private Long id;

    private Long userId;

    @NotNull(message = "学生ID不能为空", groups = {QueryGroup.NewQueryGroup.class})
    private Long studentId;

    private String stuName;

    private String courseName;

    private String courseRemark;

    private Long courseStatus;

    private Boolean isShare;

    private Long currentPage;

    private Long pageSize;
}
