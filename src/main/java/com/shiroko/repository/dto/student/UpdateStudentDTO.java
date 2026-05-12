package com.shiroko.repository.dto.student;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shiroko.repository.dto.parent.UpdateParentDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

/**
 * Description: 更新学生信息DTO
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/4/29 下午6:31
 */
@Data
public class UpdateStudentDTO {

    private Long id;

    @NotNull(message = "学生姓名不能为空")
    private String studentName;

    @NotNull(message = "学生性别不能为空")
    private Long sex;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate birth;

    private String school;

    private String address;

    private UpdateParentDTO primaryParent;

    private UpdateParentDTO secondaryParent;

}
