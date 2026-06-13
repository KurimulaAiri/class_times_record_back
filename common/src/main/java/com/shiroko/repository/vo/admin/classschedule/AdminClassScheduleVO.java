package com.shiroko.repository.vo.admin.classschedule;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminClassScheduleVO {
    private Long id;
    private Long classId;
    private String startDateStr;
    private String endDateStr;
    private Long dayOfWeek;
    private String startTimeStr;
    private String endTimeStr;
    private String remark;
    private String createTimeStr;
    private String updateTimeStr;
}
