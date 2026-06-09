package com.shiroko.repository.dto.student;

import com.shiroko.repository.dto.parent.ParentDTO;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Description: 学生数据库实体类的DTO类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/4/29 下午7:33
 */
@Data
public class StudentDTO {

    private Long id;

    private String avatar;

    private String studentName;

    private Long institutionId;

    private Long sex;

    private LocalDate birth;

    private String school;

    private String address;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String relation;

    private ParentDTO primaryParent;

    private Long courseTotalTime;

    private Long courseRestTime;
}
