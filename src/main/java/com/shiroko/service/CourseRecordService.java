package com.shiroko.service;

import com.shiroko.repository.dto.QueryCourseRecordDTO;
import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.entity.CourseRecord;
import com.shiroko.repository.vo.QueryCourseRecordVO;

import java.util.List;

/**
 * Description: TODO
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/2/7 上午1:22
 */
public interface CourseRecordService {
    /**
     * 按特定信息获取对应课程记录
     * @return 所有课程记录列表
     */
    ResponseDTO<QueryCourseRecordVO> getCourseRecords(QueryCourseRecordDTO dto);
}
