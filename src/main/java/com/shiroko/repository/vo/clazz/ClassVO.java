package com.shiroko.repository.vo.clazz;

import com.shiroko.repository.vo.courserecord.CourseRecordVO;
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

    private Long courseId;

    private String courseName;

    private CourseRecordVO courseRecord;

    private Long courseType;

    private String createTimeStr;

    private String updateTimeStr;

}
