package com.shiroko.repository.vo.admin.student;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AdminStudentListVO {
    private List<AdminStudentVO> list;
    private Long total;
}
