package com.shiroko.repository.vo.admin.courserecord;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AdminCourseRecordListVO {
    private List<AdminCourseRecordVO> list;
    private Long total;
}
