package com.shiroko.repository.vo.admin.teacher;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AdminTeacherListVO {
    private List<AdminTeacherVO> list;
    private Long total;
}
