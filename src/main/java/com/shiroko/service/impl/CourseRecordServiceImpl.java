package com.shiroko.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.shiroko.mapper.CourseRecordMapper;
import com.shiroko.repository.dto.QueryCourseRecordDTO;
import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.entity.CourseRecord;
import com.shiroko.repository.vo.QueryCourseRecordVO;
import com.shiroko.service.CourseRecordService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description: TODO
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/2/7 上午1:24
 */
@Service
public class CourseRecordServiceImpl implements CourseRecordService {

    private final CourseRecordMapper courseRecordMapper;

    private CourseRecordServiceImpl(CourseRecordMapper courseRecordMapper) {
        this.courseRecordMapper = courseRecordMapper;
    }

    @Override
    public ResponseDTO<List<QueryCourseRecordVO>> getCourseRecords(QueryCourseRecordDTO dto) {
        QueryWrapper<CourseRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", dto.getUserId());
        queryWrapper.eq("course_id", dto.getCourseId());
        List<CourseRecord> courseRecords = courseRecordMapper.selectList(queryWrapper);
        return ResponseDTO.success(courseRecords.stream().map(CourseRecord::toQueryCourseRecordVO).toList());
    }
}
