package com.shiroko.repository.dto.student;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate birth;

    private String school;

    private String address;

}
