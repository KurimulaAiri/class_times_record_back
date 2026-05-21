package com.shiroko.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shiroko.common.enums.ResultCode;
import com.shiroko.context.UserContext;
import com.shiroko.converter.CourseRecordConverter;
import com.shiroko.exception.BusinessException;
import com.shiroko.mapper.CourseMapper;
import com.shiroko.mapper.CourseRecordMapper;
import com.shiroko.mapper.PermissionRecordMapper;
import com.shiroko.mapper.RecordMapper;
import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.dto.UserDTO;
import com.shiroko.repository.dto.clazz.DeductClassDTO;
import com.shiroko.repository.dto.courserecord.*;
import com.shiroko.repository.entity.Course;
import com.shiroko.repository.entity.CourseRecord;
import com.shiroko.repository.entity.PermissionRecord;
import com.shiroko.repository.entity.Record;
import com.shiroko.repository.vo.courserecord.CourseRecordVO;
import com.shiroko.repository.vo.courserecord.DeductCourseRecordVO;
import com.shiroko.repository.vo.courserecord.QueryCourseRecordVO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseRecordServiceImplTest {

    @Mock
    private CourseRecordMapper courseRecordMapper;

    @Mock
    private CourseRecordConverter courseRecordConverter;

    @Mock
    private PermissionRecordMapper permissionRecordMapper;

    @Mock
    private CourseMapper courseMapper;

    @Mock
    private RecordMapper recordMapper;

    @InjectMocks
    private CourseRecordServiceImpl courseRecordService;

    @BeforeEach
    void setUp() {
        UserDTO user = new UserDTO(2L);
        user.setId(1L);
        UserContext.setUser(user);
    }

    @AfterEach
    void tearDown() {
        UserContext.remove();
    }

    @Test
    @DisplayName("添加课程记录应成功插入并返回success")
    void addCourseRecord_shouldInsertAndReturnSuccess() {
        InsertCourseRecordDTO dto = new InsertCourseRecordDTO();
        dto.setStuName("张三");
        dto.setCourseName("数学");
        dto.setCourseTotalTime(20L);
        dto.setCourseRestTime(20L);

        CourseRecord mockEntity = new CourseRecord().setId(100L);
        when(courseRecordConverter.insertDtoToPojo(dto)).thenReturn(mockEntity);
        when(courseRecordMapper.insert(mockEntity)).thenReturn(1);
        when(permissionRecordMapper.insert(any(PermissionRecord.class))).thenReturn(1);

        ResponseDTO<Object> result = courseRecordService.addCourseRecord(dto);

        assertEquals(Long.valueOf(200L), result.getCode());
        assertTrue(result.getMessage().contains("添加成功"));
        assertEquals(Long.valueOf(1L), mockEntity.getCourseOwnerUserId());

        verify(courseRecordMapper).insert(mockEntity);
        verify(permissionRecordMapper).insert(any(PermissionRecord.class));
    }

    @Test
    @DisplayName("逻辑删除课程记录应返回success")
    void deleteCourseRecord_shouldSetIsDeleteAndReturnSuccess() {
        DeleteCourseRecordDTO dto = new DeleteCourseRecordDTO(50L);
        when(courseRecordMapper.updateById((CourseRecord) any())).thenReturn(1);

        ResponseDTO<Object> result = courseRecordService.deleteCourseRecord(dto);

        assertEquals(Long.valueOf(200L), result.getCode());
        verify(courseRecordMapper).updateById(argThat((CourseRecord cr) ->
                cr.getId().equals(50L) && Boolean.TRUE.equals(cr.getIsDelete())));
    }

    @Test
    @DisplayName("更新课程记录应返回success")
    void updateCourseRecord_shouldUpdateAndReturnSuccess() {
        UpdateCourseRecordDTO dto = new UpdateCourseRecordDTO();
        dto.setId(1L);
        dto.setCourseTotalTime(30L);
        dto.setCourseRestTime(15L);
        dto.setCourseStatus(1L);

        CourseRecord mockEntity = new CourseRecord().setId(1L);
        when(courseRecordConverter.updateDtoToPojo(dto)).thenReturn(mockEntity);
        when(courseRecordMapper.updateById((CourseRecord) any())).thenReturn(1);

        ResponseDTO<Object> result = courseRecordService.updateCourseRecord(dto);

        assertEquals(Long.valueOf(200L), result.getCode());
        verify(courseRecordMapper).updateById(mockEntity);
    }

    @Test
    @DisplayName("扣课-单课程扣减成功应返回影响行数")
    void deductByStudentId_withSingleCourse_shouldDeductSuccessfully() {
        DeductClassDTO deductClass = new DeductClassDTO(1L, 10L, 3, "扣课备注");
        DeductCourseRecordDTO dto = new DeductCourseRecordDTO(100L, "整单备注",
                List.of(deductClass));

        when(courseRecordMapper.updateRestTime(any(CourseRecord.class), eq(3))).thenReturn(1);
        when(recordMapper.insert(any(Record.class))).thenReturn(1);

        ResponseDTO<DeductCourseRecordVO> result = courseRecordService.deductByStudentId(dto);

        assertEquals(Long.valueOf(200L), result.getCode());
        assertEquals(Integer.valueOf(1), result.getData().getRes());
        verify(recordMapper).insert(any(Record.class));
    }

    @Test
    @DisplayName("扣课-同课程多班级应聚合扣减数量")
    void deductByStudentId_withSameCourseMultipleClasses_shouldAggregateDeductCount() {
        DeductClassDTO classA = new DeductClassDTO(1L, 10L, 2, "A班");
        DeductClassDTO classB = new DeductClassDTO(2L, 10L, 3, "B班");
        DeductCourseRecordDTO dto = new DeductCourseRecordDTO(100L, "聚合扣课",
                List.of(classA, classB));

        when(courseRecordMapper.updateRestTime(any(CourseRecord.class), eq(5))).thenReturn(1);
        when(recordMapper.insert(any(Record.class))).thenReturn(1);

        ResponseDTO<DeductCourseRecordVO> result = courseRecordService.deductByStudentId(dto);

        assertEquals(Long.valueOf(200L), result.getCode());
        assertEquals(Integer.valueOf(1), result.getData().getRes());
    }

    @Test
    @DisplayName("扣课-余额不足应抛出BusinessException")
    void deductByStudentId_whenBalanceNotEnough_shouldThrowBusinessException() {
        DeductClassDTO deductClass = new DeductClassDTO(1L, 10L, 100, "超额扣课");
        DeductCourseRecordDTO dto = new DeductCourseRecordDTO(100L, "超额",
                List.of(deductClass));

        Course mockCourse = new Course();
        mockCourse.setCourseName("数学");
        when(courseRecordMapper.updateRestTime(any(CourseRecord.class), eq(100))).thenReturn(0);
        when(courseMapper.selectById(10L)).thenReturn(mockCourse);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> courseRecordService.deductByStudentId(dto));

        assertEquals(ResultCode.COURSE_BALANCE_NOT_ENOUGH.getCode(), exception.getCode().intValue());
        assertTrue(exception.getMessage().contains("数学"));
        verify(recordMapper, never()).insert(any(Record.class));
    }

    @Test
    @DisplayName("getCourseRecords查询应注入权限类型")
    void getCourseRecords_shouldInjectPermissionType() {
        QueryCourseRecordDTO dto = new QueryCourseRecordDTO();
        dto.setCurrentPage(1L);
        dto.setPageSize(10L);

        CourseRecordVO vo1 = new CourseRecordVO();
        vo1.setId(1L);
        CourseRecordVO vo2 = new CourseRecordVO();
        vo2.setId(2L);
        List<CourseRecordVO> voList = List.of(vo1, vo2);

        doAnswer(invocation -> {
            com.baomidou.mybatisplus.core.metadata.IPage<CourseRecordVO> page = invocation.getArgument(0);
            page.setRecords(voList);
            page.setTotal(2);
            return page;
        }).when(courseRecordMapper).selectCourseCustomPage(any(), eq(dto));

        PermissionRecord perm1 = new PermissionRecord().setCourseRecordId(1L).setPermissionType(1L);
        PermissionRecord perm2 = new PermissionRecord().setCourseRecordId(2L).setPermissionType(2L);
        when(permissionRecordMapper.selectList(any(LambdaQueryWrapper.class)))
                .thenReturn(List.of(perm1, perm2));

        ResponseDTO<QueryCourseRecordVO> result = courseRecordService.getCourseRecords(dto);

        assertEquals(Long.valueOf(200L), result.getCode());
        assertEquals(Long.valueOf(2L), result.getData().getTotal());
        assertEquals(Long.valueOf(1L), vo1.getPermissionType());
        assertEquals(Long.valueOf(2L), vo2.getPermissionType());
    }

    @Test
    @DisplayName("getCourseRecords无记录时应返回空列表")
    void getCourseRecords_whenNoRecords_shouldReturnEmptyList() {
        QueryCourseRecordDTO dto = new QueryCourseRecordDTO();
        dto.setCurrentPage(1L);
        dto.setPageSize(10L);

        doAnswer(invocation -> {
            com.baomidou.mybatisplus.core.metadata.IPage<CourseRecordVO> page = invocation.getArgument(0);
            page.setRecords(List.of());
            page.setTotal(0);
            return page;
        }).when(courseRecordMapper).selectCourseCustomPage(any(), eq(dto));

        ResponseDTO<QueryCourseRecordVO> result = courseRecordService.getCourseRecords(dto);

        assertEquals(Long.valueOf(200L), result.getCode());
        assertEquals(Long.valueOf(0L), result.getData().getTotal());
        assertTrue(result.getData().getCourseRecords().isEmpty());
        verify(permissionRecordMapper, never()).selectList(any());
    }

    @Test
    @DisplayName("newGetCourseRecords查询应返回转换后的VO列表")
    void newGetCourseRecords_shouldReturnConvertedVOList() {
        QueryCourseRecordDTO dto = new QueryCourseRecordDTO();
        dto.setCurrentPage(1L);
        dto.setPageSize(10L);

        CourseRecord entity1 = new CourseRecord().setId(1L);
        CourseRecord entity2 = new CourseRecord().setId(2L);
        List<CourseRecord> entityList = List.of(entity1, entity2);

        doAnswer(invocation -> {
            com.baomidou.mybatisplus.core.metadata.IPage<CourseRecord> page = invocation.getArgument(0);
            page.setRecords(entityList);
            page.setTotal(2);
            return page;
        }).when(courseRecordMapper).selectCourseRecords(any(), eq(dto));

        CourseRecordVO vo1 = new CourseRecordVO();
        vo1.setId(1L);
        CourseRecordVO vo2 = new CourseRecordVO();
        vo2.setId(2L);
        when(courseRecordConverter.pojoListToVOList(entityList)).thenReturn(List.of(vo1, vo2));

        PermissionRecord perm = new PermissionRecord().setCourseRecordId(1L).setPermissionType(1L);
        when(permissionRecordMapper.selectList(any(LambdaQueryWrapper.class)))
                .thenReturn(List.of(perm));

        ResponseDTO<QueryCourseRecordVO> result = courseRecordService.newGetCourseRecords(dto);

        assertEquals(Long.valueOf(200L), result.getCode());
        assertEquals(Long.valueOf(2L), result.getData().getTotal());
        assertEquals(2, result.getData().getCourseRecords().size());
    }
}
