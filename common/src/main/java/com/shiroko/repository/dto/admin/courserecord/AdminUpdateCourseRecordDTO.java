package com.shiroko.repository.dto.admin.courserecord;

import lombok.Data;

@Data
public class AdminUpdateCourseRecordDTO {
    private Long id;
    private Long courseRestTime;
    private Long courseTotalTime;
    private Long courseStatus;
    private String courseRemark;
}
