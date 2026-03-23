package com.shiroko.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiroko.converter.RecordConverter;
import com.shiroko.mapper.CourseRecordMapper;
import com.shiroko.mapper.RecordMapper;
import com.shiroko.repository.dto.InsertRecordDTO;
import com.shiroko.repository.dto.InsertRecordsDTO;
import com.shiroko.repository.dto.QueryRecordDTO;
import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.entity.CourseRecord;
import com.shiroko.repository.entity.Record;
import com.shiroko.repository.vo.QueryRecordVO;
import com.shiroko.service.RecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Description: 记录服务实现类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/3/22 下午22:50
 */
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class RecordServiceImpl extends ServiceImpl<RecordMapper, Record> implements RecordService {

    private final RecordConverter recordConverter;

    private final RecordMapper recordMapper;

    private final CourseRecordMapper courseRecordMapper;

    @Override
    public ResponseDTO<Object> insertRecord(InsertRecordDTO insertRecordDTO) {
        Record record = recordConverter.insertDtoToPojo(insertRecordDTO);
        int result = recordMapper.insert(record);
        if (result > 0) {
            return ResponseDTO.success("插入成功，影响行数为" + result);
        }
        return ResponseDTO.fail("插入失败");
    }

    @Override
    public ResponseDTO<Object> insertRecords(InsertRecordsDTO insertRecordsDTO) {
        int recordResult = 0;
        // 新增记录
        for (Long courseRecordId : insertRecordsDTO.getCourseRecordIdList()) {
            Record record = new Record();
            record.setCourseRecordId(courseRecordId)
                    .setRecordTime(insertRecordsDTO.getRecordTime())
                    .setRecordRemark(insertRecordsDTO.getRecordRemark())
                    .setRecordType(insertRecordsDTO.getRecordType())
                    .setRecordChange(insertRecordsDTO.getRecordChange());
            recordResult += recordMapper.insert(record);

            // 更新课时
            // 查询当前课时记录
            CourseRecord oldCourseRecord = courseRecordMapper.selectById(courseRecordId);
            // 消课
            if (record.getRecordType() == 1L) {
                courseRecordMapper.updateById(new CourseRecord()
                        .setId(courseRecordId)
                        .setCourseLastTime(record.getRecordTime())
                        .setCourseRestTime(oldCourseRecord.getCourseRestTime() - record.getRecordChange())
                );
                // 增加课时
            } else if (record.getRecordType() == 2L) {
                courseRecordMapper.updateById(new CourseRecord()
                        .setId(courseRecordId)
                        .setCourseRestTime(oldCourseRecord.getCourseRestTime() + record.getRecordChange())
                        .setCourseTotalTime(oldCourseRecord.getCourseTotalTime() + record.getRecordChange())
                );
            }
        }
        if (recordResult > 0) {
            return ResponseDTO.success("插入成功，影响行数为" + recordResult);
        }
        return ResponseDTO.fail("插入失败");
    }

    @Override
    public ResponseDTO<QueryRecordVO> getRecord(QueryRecordDTO queryRecordDTO) {
        Page<Record> recordPage = recordMapper.selectPage(new Page<>(queryRecordDTO.getCurrentPage(), queryRecordDTO.getPageSize()), new LambdaQueryWrapper<Record>()
                .eq(Record::getCourseRecordId, queryRecordDTO.getCourseRecordId())
                .orderByDesc(Record::getRecordTime));
        List<Record> recordList = recordPage.getRecords();
        QueryRecordVO queryRecordVO = new QueryRecordVO()
                .setRecordVOList(recordConverter.pojoListToVOList(recordList))
                .setTotal(recordPage.getTotal());
        return ResponseDTO.success(queryRecordVO);
    }
}




