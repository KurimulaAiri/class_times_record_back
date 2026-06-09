package com.shiroko.repository.vo.course;

import lombok.Data;

import java.util.List;

/**
 * Description: 查询课程VO类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/9 上午12:37
 */
@Data
public class QueryCourseVO {

    private List<CourseVO> courses;

    private Long total;

}
