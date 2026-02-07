package com.shiroko.repository.dto;

import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "stuName 不能为空")
    private String stuName;

    @NotBlank(message = "courseName 不能为空")
    private String courseName;

    private Long currentPage;

    private Long pageSize;
}
