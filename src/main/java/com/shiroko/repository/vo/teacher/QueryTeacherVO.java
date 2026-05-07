package com.shiroko.repository.vo.teacher;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Description: 查询教师VO类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/7 下午6:31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryTeacherVO {

    private List<TeacherVO> teachers;

    private Long total;
}
