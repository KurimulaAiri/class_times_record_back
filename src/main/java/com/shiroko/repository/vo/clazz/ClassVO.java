package com.shiroko.repository.vo.clazz;

import lombok.Data;

/**
 * Description: 班级VO类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/7 下午9:04
 */
@Data
public class ClassVO {

    private Long id;

    private String className;

    private Long studentCount;

    private Long studentMaxCount;

    private String username;

    private String courseName;

    private Long courseType;

    private String createTimeStr;

    private String updateTimeStr;

}
