package com.shiroko.repository.dto.student;

import com.shiroko.repository.entity.Student;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Description: 学生数据库实体类的DTO类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/4/29 下午7:33
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StudentDTO extends Student {

    private String relation;

}
