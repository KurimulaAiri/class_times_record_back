package com.shiroko.repository.dto.courserecord;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shiroko.repository.dto.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Description: 更新课程记录DTO
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/3/21 下午8:19
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCourseRecordDTO extends BaseDTO {
    private Long id;
    private Long courseTotalTime;
    private Long courseRestTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expireTime;
    private Long courseStatus;
    private String courseName;
    private String courseRemark;
    private Long studentId;
    private String stuName;
   }
