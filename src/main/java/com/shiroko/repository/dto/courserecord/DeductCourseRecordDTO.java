package com.shiroko.repository.dto.courserecord;

import com.shiroko.repository.dto.clazz.DeductClassDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private Long studentId;

    private String remark;

    private List<DeductClassDTO> classes;

}
