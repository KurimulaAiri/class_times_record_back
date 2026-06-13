package com.shiroko.repository.dto.admin.courserecord;

import lombok.Data;

@Data
public class AdminInsertCourseRecordDTO {
    private Long studentId;
    private Long courseId;
    private Long courseTotalTime;
    private Long courseRestTime;
    private String expireTime;
    private Long courseStatus;
    private String courseRemark;
}
