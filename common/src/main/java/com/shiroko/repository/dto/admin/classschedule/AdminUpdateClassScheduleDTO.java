package com.shiroko.repository.dto.admin.classschedule;

import lombok.Data;

@Data
public class AdminUpdateClassScheduleDTO {
    private Long id;
    private String startDate;
    private String endDate;
    private Integer dayOfWeek;
    private String startTime;
    private String endTime;
    private String remark;
}
