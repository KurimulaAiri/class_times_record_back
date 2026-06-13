package com.shiroko.repository.vo.admin.courserecord;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminCourseRecordVO {
    private Long id;
    private Long studentId;
    private Long courseId;
    private Long courseTotalTime;
    private Long courseRestTime;
    private String courseLastTimeStr;
    private String expireTimeStr;
    private Long courseStatus;
    private Long courseOwnerUserId;
    private String courseRemark;
    private Boolean isDelete;
    private String createTimeStr;
    private String updateTimeStr;
}
