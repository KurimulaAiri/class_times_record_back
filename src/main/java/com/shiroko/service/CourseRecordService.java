package com.shiroko.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.dto.courserecord.*;
import com.shiroko.repository.entity.CourseRecord;
import com.shiroko.repository.vo.courserecord.DeductCourseRecordVO;
import com.shiroko.repository.vo.courserecord.QueryCourseRecordVO;

/**
 * Description: 课程记录服务接口
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/2/7 上午1:22
 */
public interface CourseRecordService extends IService<CourseRecord> {
    /**
     * 按特定信息获取对应课程记录
     * @return 所有课程记录列表
     */
    ResponseDTO<QueryCourseRecordVO> getCourseRecords(QueryCourseRecordDTO dto);

    ResponseDTO<Object> addCourseRecord(InsertCourseRecordDTO dto);

    ResponseDTO<Object> deleteCourseRecord(DeleteCourseRecordDTO dto);

    ResponseDTO<Object> updateCourseRecord(UpdateCourseRecordDTO dto);

    ResponseDTO<QueryCourseRecordVO> newGetCourseRecords(QueryCourseRecordDTO dto);

    ResponseDTO<DeductCourseRecordVO> deductByStudentId(DeductCourseRecordDTO deductCourseRecordDTO);

    ResponseDTO<DeductCourseRecordVO> deductByCourseId(DeductCourseRecordDTO deductCourseRecordDTO);

    ResponseDTO<Object> insertCourseRecord(InsertCourseRecordDTO insertCourseRecordDTO);

    ResponseDTO<DeductCourseRecordVO> deductByClassId(DeductCourseRecordDTO deductCourseRecordDTO);
}
