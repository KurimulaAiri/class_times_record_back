package com.shiroko.repository.dto.courserecord;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shiroko.repository.dto.clazz.DeductClassDTO;
import com.shiroko.repository.dto.courserecord.validategroup.DeductGroup;
import com.shiroko.repository.dto.student.DeductStudentDTO;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Description: 扣课记录DTO类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/15 下午6:46
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeductCourseRecordDTO {

    @NotNull(message = "学生ID不能为空", groups = DeductGroup.ByStudent.class)
    private Long studentId;

    @NotNull(message = "课程ID不能为空", groups = DeductGroup.ByCourse.class)
    private Long courseId;

    // 加上这行注解，让 Jackson 认识带空格的经典时间格式
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @NotNull(message = "记录课时间不能为空", groups = {DeductGroup.ByStudent.class, DeductGroup.ByCourse.class})
    private LocalDateTime recordTime;

    private String remark;

    @NotNull(message = "课程列表不能为空", groups = DeductGroup.ByStudent.class)
    private List<DeductClassDTO> classes;

    @NotNull(message = "学生列表不能为空", groups = DeductGroup.ByCourse.class)
    private List<DeductStudentDTO> students;

}
