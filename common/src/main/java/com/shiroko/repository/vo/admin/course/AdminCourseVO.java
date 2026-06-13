package com.shiroko.repository.vo.admin.course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminCourseVO {
    private Long id;
    private String courseName;
    private Long courseType;
    private Long institutionId;
    private Boolean isAvailable;
    private String createTimeStr;
    private String updateTimeStr;
}
