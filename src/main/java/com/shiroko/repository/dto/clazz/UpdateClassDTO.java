package com.shiroko.repository.dto.clazz;

import com.shiroko.repository.dto.student.StudentDTO;
import lombok.Data;

import java.util.List;

/**
 * Description: 更新班级信息DTO
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/15 上午1:39
 */
@Data
public class UpdateClassDTO {

    private Long classId;

    private List<StudentDTO> students;

}
