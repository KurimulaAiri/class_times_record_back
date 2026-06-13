package com.shiroko.repository.vo.admin.course;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AdminCourseListVO {
    private List<AdminCourseVO> list;
    private Long total;
}
