package com.shiroko.repository.vo.admin.teacher;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminTeacherVO {
    private Long teacherId;
    private Long institutionId;
    private Boolean isAvailable;
    private String username;
    private String createTimeStr;
    private String updateTimeStr;
}
