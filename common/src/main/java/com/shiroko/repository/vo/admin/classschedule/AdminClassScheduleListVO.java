package com.shiroko.repository.vo.admin.classschedule;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AdminClassScheduleListVO {
    private List<AdminClassScheduleVO> list;
    private Long total;
}
