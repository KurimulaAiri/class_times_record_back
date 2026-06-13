package com.shiroko.repository.vo.admin.clazz;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminClassVO {
    private Long id;
    private Long courseId;
    private String className;
    private Long status;
    private Long studentCount;
    private Long studentMaxCount;
    private String createTimeStr;
    private String updateTimeStr;
}
