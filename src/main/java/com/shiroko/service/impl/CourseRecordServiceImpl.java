package com.shiroko.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shiroko.converter.CourseRecordConverter;
import com.shiroko.mapper.CourseRecordMapper;
import com.shiroko.repository.dto.QueryCourseRecordDTO;
import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.entity.CourseRecord;
import com.shiroko.repository.vo.CourseRecordVO;
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

    private final CourseRecordConverter courseRecordConverter;

    private CourseRecordServiceImpl(CourseRecordMapper courseRecordMapper, CourseRecordConverter courseRecordConverter) {
        this.courseRecordMapper = courseRecordMapper;
        this.courseRecordConverter = courseRecordConverter;
    }

    @Override
    public ResponseDTO<QueryCourseRecordVO> getCourseRecords(QueryCourseRecordDTO dto) {
        LambdaQueryWrapper<CourseRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CourseRecord::getStuName, dto.getStuName());
        queryWrapper.eq(CourseRecord::getCourseName, dto.getCourseName());
        queryWrapper.eq(CourseRecord::getIsDelete, false);
        List<CourseRecord> courseRecords = courseRecordMapper.selectList(queryWrapper);
        List<CourseRecordVO> voList = courseRecordConverter.pojoListToVOList(courseRecords);
        QueryCourseRecordVO vo = new QueryCourseRecordVO(voList);
        return ResponseDTO.success(vo);
    }
}
