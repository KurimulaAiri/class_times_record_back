package com.shiroko.repository.vo.course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description: 插入课程视图对象：包含课程的基本信息
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/25 下午3:28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InsertCourseVO {
    private Long courseId;
}
