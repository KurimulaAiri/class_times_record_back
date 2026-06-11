package com.shiroko.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shiroko.converter.RecordConverter;
import com.shiroko.mapper.CourseRecordMapper;
import com.shiroko.mapper.RecordMapper;
import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.dto.record.InsertRecordDTO;
import com.shiroko.repository.dto.record.InsertRecordsDTO;
import com.shiroko.repository.dto.record.QueryRecordDTO;
import com.shiroko.repository.entity.CourseRecord;
import com.shiroko.repository.entity.Record;
import com.shiroko.repository.vo.record.QueryRecordVO;
import com.shiroko.repository.vo.record.RecordVO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecordServiceImplTest {

    @Mock
    private RecordMapper recordMapper;

    @Mock
    private CourseRecordMapper courseRecordMapper;

    @Mock
    private RecordConverter recordConverter;

    @InjectMocks
    private RecordServiceImpl recordService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("插入单条记录成功应返回success")
    void insertRecord_whenInsertSucceeds_shouldReturnSuccess() {
        InsertRecordDTO dto = new InsertRecordDTO();
        dto.setCourseRecordId(1L);
        dto.setRecordTime(LocalDateTime.now());
        dto.setRecordType(1L);
        dto.setRecordRemark("测试备注");
        dto.setRecordChange(2L);

        Record mockRecord = new Record();
        when(recordConverter.insertDtoToPojo(dto)).thenReturn(mockRecord);
        when(recordMapper.insert(mockRecord)).thenReturn(1);

        ResponseDTO<Object> result = recordService.insertRecord(dto);

        assertEquals(Long.valueOf(200L), result.getCode());
        assertTrue(result.getData().toString().contains("插入成功"));
        verify(recordConverter).insertDtoToPojo(dto);
        verify(recordMapper).insert(mockRecord);
    }

    @Test
    @DisplayName("插入单条记录失败应返回fail")
    void insertRecord_whenInsertFails_shouldReturnFail() {
        InsertRecordDTO dto = new InsertRecordDTO();
        dto.setCourseRecordId(1L);
        dto.setRecordTime(LocalDateTime.now());
        dto.setRecordType(1L);
        dto.setRecordChange(2L);

        Record mockRecord = new Record();
        when(recordConverter.insertDtoToPojo(dto)).thenReturn(mockRecord);
        when(recordMapper.insert(mockRecord)).thenReturn(0);

        ResponseDTO<Object> result = recordService.insertRecord(dto);

        assertEquals(Long.valueOf(400L), result.getCode());
        assertEquals("插入失败", result.getMessage());
    }

    @Test
    @DisplayName("批量插入记录-扣课类型(type=1)应正确减少剩余课时")
    void insertRecords_withDeductType_shouldUpdateRestTime() {
        InsertRecordsDTO dto = new InsertRecordsDTO();
        dto.setCourseRecordIdList(new Long[]{1L, 2L});
        dto.setRecordType(1L);
        dto.setRecordChange(3L);
        dto.setRecordTime(LocalDateTime.now());
        dto.setRecordRemark("扣课");

        CourseRecord cr1 = new CourseRecord().setId(1L).setCourseRestTime(10L);
        CourseRecord cr2 = new CourseRecord().setId(2L).setCourseRestTime(5L);
        when(courseRecordMapper.selectById(1L)).thenReturn(cr1);
        when(courseRecordMapper.selectById(2L)).thenReturn(cr2);
        when(recordMapper.insert(any(Record.class))).thenReturn(1);
        when(courseRecordMapper.updateById(any(CourseRecord.class))).thenReturn(1);

        ResponseDTO<Object> result = recordService.insertRecords(dto);

        assertEquals(Long.valueOf(200L), result.getCode());

        ArgumentCaptor<CourseRecord> crCaptor = ArgumentCaptor.forClass(CourseRecord.class);
        verify(courseRecordMapper, times(2)).updateById(crCaptor.capture());
        List<CourseRecord> updatedCRs = crCaptor.getAllValues();

        assertEquals(Long.valueOf(7L), updatedCRs.get(0).getCourseRestTime());
        assertEquals(Long.valueOf(2L), updatedCRs.get(1).getCourseRestTime());
    }

    @Test
    @DisplayName("批量插入记录-增加课时类型(type=2)应正确增加剩余课时和总课时")
    void insertRecords_withAddType_shouldUpdateRestAndTotalTime() {
        InsertRecordsDTO dto = new InsertRecordsDTO();
        dto.setCourseRecordIdList(new Long[]{1L});
        dto.setRecordType(2L);
        dto.setRecordChange(5L);
        dto.setRecordTime(LocalDateTime.now());
        dto.setRecordRemark("增加课时");

        CourseRecord cr = new CourseRecord().setId(1L).setCourseRestTime(10L).setCourseTotalTime(20L);
        when(courseRecordMapper.selectById(1L)).thenReturn(cr);
        when(recordMapper.insert(any(Record.class))).thenReturn(1);
        when(courseRecordMapper.updateById(any(CourseRecord.class))).thenReturn(1);

        ResponseDTO<Object> result = recordService.insertRecords(dto);

        assertEquals(Long.valueOf(200L), result.getCode());

        ArgumentCaptor<CourseRecord> crCaptor = ArgumentCaptor.forClass(CourseRecord.class);
        verify(courseRecordMapper).updateById(crCaptor.capture());
        CourseRecord updatedCR = crCaptor.getValue();

        assertEquals(Long.valueOf(15L), updatedCR.getCourseRestTime());
        assertEquals(Long.valueOf(25L), updatedCR.getCourseTotalTime());
    }

    @Test
    @DisplayName("批量插入记录全部失败应返回fail")
    void insertRecords_whenAllFail_shouldReturnFail() {
        InsertRecordsDTO dto = new InsertRecordsDTO();
        dto.setCourseRecordIdList(new Long[]{1L});
        dto.setRecordType(1L);
        dto.setRecordChange(3L);
        dto.setRecordTime(LocalDateTime.now());

        CourseRecord cr = new CourseRecord().setId(1L).setCourseRestTime(10L);
        when(recordMapper.insert(any(Record.class))).thenReturn(0);
        when(courseRecordMapper.selectById(1L)).thenReturn(cr);

        ResponseDTO<Object> result = recordService.insertRecords(dto);

        assertEquals(Long.valueOf(400L), result.getCode());
    }

    @Test
    @DisplayName("分页查询记录应返回正确分页数据")
    void getRecord_shouldReturnPagedRecords() {
        QueryRecordDTO dto = new QueryRecordDTO();
        dto.setCourseRecordId(1L);
        dto.setCurrentPage(1);
        dto.setPageSize(10);

        Record record1 = new Record().setId(1L).setCourseRecordId(1L);
        Record record2 = new Record().setId(2L).setCourseRecordId(1L);
        List<Record> recordList = List.of(record1, record2);

        Page<Record> mockPage = new Page<>(1, 10);
        mockPage.setRecords(recordList);
        mockPage.setTotal(2);

        when(recordMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class)))
                .thenReturn(mockPage);

        RecordVO vo1 = new RecordVO();
        RecordVO vo2 = new RecordVO();
        when(recordConverter.pojoListToVOList(recordList)).thenReturn(List.of(vo1, vo2));

        ResponseDTO<QueryRecordVO> result = recordService.getRecord(dto);

        assertEquals(Long.valueOf(200L), result.getCode());
        assertNotNull(result.getData());
        assertEquals(Long.valueOf(2L), result.getData().getTotal());
        assertEquals(2, result.getData().getRecords().size());
    }

    @Test
    @DisplayName("分页查询无记录时应返回空列表")
    void getRecord_whenNoRecords_shouldReturnEmptyList() {
        QueryRecordDTO dto = new QueryRecordDTO();
        dto.setCourseRecordId(999L);
        dto.setCurrentPage(1);
        dto.setPageSize(10);

        Page<Record> mockPage = new Page<>(1, 10);
        mockPage.setRecords(List.of());
        mockPage.setTotal(0);

        when(recordMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class)))
                .thenReturn(mockPage);
        when(recordConverter.pojoListToVOList(List.of())).thenReturn(List.of());

        ResponseDTO<QueryRecordVO> result = recordService.getRecord(dto);

        assertEquals(Long.valueOf(200L), result.getCode());
        assertEquals(Long.valueOf(0L), result.getData().getTotal());
        assertTrue(result.getData().getRecords().isEmpty());
    }
}