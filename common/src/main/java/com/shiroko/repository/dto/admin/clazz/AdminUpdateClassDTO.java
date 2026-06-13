package com.shiroko.repository.dto.admin.clazz;

import lombok.Data;

@Data
public class AdminUpdateClassDTO {
    private Long classId;
    private String className;
    private Integer studentMaxCount;
    private Long status;
    private Boolean onlyUpdateClassOwn;
}
