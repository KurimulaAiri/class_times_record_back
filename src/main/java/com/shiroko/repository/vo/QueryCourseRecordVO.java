package com.shiroko.repository.vo;

import com.shiroko.repository.entity.CourseRecord;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Description: TODO
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/2/7 上午1:30
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryCourseRecordVO extends BaseVO {

    private List<CourseRecordVO> courseRecords;

}
