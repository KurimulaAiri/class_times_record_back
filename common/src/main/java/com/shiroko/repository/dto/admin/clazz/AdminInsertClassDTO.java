package com.shiroko.repository.dto.admin.clazz;

import lombok.Data;

import java.util.List;

@Data
public class AdminInsertClassDTO {
    private String className;
    private Long courseId;
    private Integer studentMaxCount;
    private Long status;
    private List<Long> teacherIds;
}
