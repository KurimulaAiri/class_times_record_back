package com.shiroko.repository.dto.record;

import com.shiroko.repository.entity.Course;
import com.shiroko.repository.entity.CourseRecord;
import com.shiroko.repository.entity.Student;
import com.shiroko.repository.entity.Teacher;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Description: 记录DTO
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/6/5 下午9:50
 */
@Data
public class RecordDTO {
    private Long id;
    private Long courseRecordId;
    private CourseRecord courseRecord;
    private Course course;
    private Student student;
    private LocalDateTime recordTime;
    private Teacher operatorTeacher;
    private String recordRemark;
    private Long recordType;
    private Long recordChange;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
