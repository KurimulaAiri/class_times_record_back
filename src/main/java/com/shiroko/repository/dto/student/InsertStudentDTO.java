package com.shiroko.repository.dto.student;

import com.shiroko.repository.dto.parent.InsertParentDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

/**
 * Description: 插入学生DTO类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/10 下午11:04
 */
@Data
public class InsertStudentDTO {

    @NotBlank(message = "学生姓名不能为空")
    private String studentName;

    @NotNull(message = "机构不能为空")
    private Long institutionId;

    @NotNull(message = "性别不能为空")
    private Long sex;

    private LocalDate birth;

    private String address;

    private String school;

    @NotNull(message = "主要家长不能为空")
    private InsertParentDTO primaryParent;

    @NotNull(message = "次要家长不能为空")
    private InsertParentDTO secondaryParent;

}
