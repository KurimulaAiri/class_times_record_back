package com.shiroko.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.shiroko.context.UserContext;
import com.shiroko.converter.CourseRecordConverter;
import com.shiroko.mapper.PermissionRecordMapper;
import com.shiroko.mapper.CourseRecordMapper;
import com.shiroko.repository.dto.*;
import com.shiroko.repository.entity.PermissionRecord;
import com.shiroko.repository.entity.CourseRecord;
import com.shiroko.repository.vo.CourseRecordVO;
import com.shiroko.repository.vo.QueryCourseRecordVO;
import com.shiroko.service.CourseRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Description: TODO
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/2/7 上午1:24
 */
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class CourseRecordServiceImpl implements CourseRecordService {

    private final CourseRecordMapper courseRecordMapper;

    private final CourseRecordConverter courseRecordConverter;

    private final PermissionRecordMapper permissionRecordMapper;

    @Override
    public ResponseDTO<QueryCourseRecordVO> getCourseRecords(QueryCourseRecordDTO dto) {
        LambdaQueryWrapper<CourseRecord> qw = new LambdaQueryWrapper<>();
        qw.eq(CourseRecord::getCourseOwnerUserId, UserContext.getUser().getId())
                .eq(StringUtils.isNotBlank(dto.getStuName()), CourseRecord::getStuName, dto.getStuName())
                .eq(StringUtils.isNotBlank(dto.getCourseName()), CourseRecord::getCourseName, dto.getCourseName())
                .eq(CourseRecord::getIsDelete, false);
        List<CourseRecord> courseRecords = courseRecordMapper.selectList(qw);
        List<CourseRecordVO> voList = courseRecordConverter.pojoListToVOList(courseRecords);
        QueryCourseRecordVO vo = new QueryCourseRecordVO(voList, courseRecords.size());
        return ResponseDTO.success(vo);
    }

    @Override
    public ResponseDTO<Object> addCourseRecord(InsertCourseRecordDTO dto) {
        CourseRecord courseRecord = courseRecordConverter.insertDtoToPojo(dto);
        courseRecord.setCourseOwnerUserId(UserContext.getUser().getId());
        int rowsInserted = courseRecordMapper.insert(
                courseRecord
        );
        PermissionRecord permissionRecord = new PermissionRecord()
                .setCourseRecordId(courseRecord.getId())
                .setUserId(UserContext.getUser().getId())
                .setPermissionType("admin");
        int rowsInsertedAdminGroupRecord = permissionRecordMapper.insert(permissionRecord);
        return ResponseDTO.success("添加成功，影响记录数：" + rowsInserted + "\n管理员分组记录数：" + rowsInsertedAdminGroupRecord, new int[]{rowsInserted, rowsInsertedAdminGroupRecord});
    }

    @Override
    public ResponseDTO<Object> deleteCourseRecord(DeleteCourseRecordDTO dto) {
        int rowsDeleted = courseRecordMapper.updateById(
                new CourseRecord()
                        .setId(dto.getCourseRecordId())
                        .setIsDelete(true)
        );
        return ResponseDTO.success("删除成功，影响记录数：" + rowsDeleted);
    }

    @Override
    public ResponseDTO<Object> updateCourseRecord(UpdateCourseRecordDTO dto) {
        CourseRecord courseRecord = courseRecordConverter.updateDtoToPojo(dto);
        System.out.println(courseRecord);
        int rowsUpdated = courseRecordMapper.updateById(
                courseRecord
        );
        return ResponseDTO.success("更新成功，影响记录数：" + rowsUpdated);
    }
}
