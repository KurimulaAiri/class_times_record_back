package com.shiroko.repository.dto.clazz;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Description: 班级DTO类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/7 下午9:18
 */
@Data
public class ClassDTO {

    private Long id;

    private String className;

    private Long studentCount;

    private Long studentMaxCount;

    private String courseName;

    private String username;

    private Long courseType;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
