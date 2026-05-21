package com.shiroko.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shiroko.converter.ParentConverter;
import com.shiroko.converter.StudentConverter;
import com.shiroko.mapper.InstitutionMapper;
import com.shiroko.mapper.ParentMapper;
import com.shiroko.mapper.ParentStudentMapper;
import com.shiroko.mapper.StudentMapper;
import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.dto.parent.InsertParentDTO;
import com.shiroko.repository.dto.parent.UpdateParentDTO;
import com.shiroko.repository.dto.student.InsertStudentDTO;
import com.shiroko.repository.dto.student.QueryStudentDTO;
import com.shiroko.repository.dto.student.StudentDTO;
import com.shiroko.repository.dto.student.UpdateStudentDTO;
import com.shiroko.repository.entity.Parent;
import com.shiroko.repository.entity.ParentStudent;
import com.shiroko.repository.entity.Student;
import com.shiroko.repository.vo.parent.ParentVO;
import com.shiroko.repository.vo.student.InsertStudentVO;
import com.shiroko.repository.vo.student.QueryStudentVO;
import com.shiroko.repository.vo.student.StudentVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {

    private final AtomicLong psIdGenerator = new AtomicLong(300L);
    @Mock
    private StudentMapper studentMapper;
    @Mock
    private InstitutionMapper institutionMapper;
    @Mock
    private ParentStudentMapper parentStudentMapper;
    @Mock
    private ParentMapper parentMapper;
    @Mock
    private StudentConverter studentConverter;
    @Mock
    private ParentConverter parentConverter;
    @InjectMocks
    private StudentServiceImpl studentService;

    @BeforeEach
    void setUp() {
        psIdGenerator.set(300L);
        lenient().doAnswer(invocation -> {
            ParentStudent ps = invocation.getArgument(0);
            if (ps.getId() == null) {
                ps.setId(psIdGenerator.getAndIncrement());
            }
            return 1;
        }).when(parentStudentMapper).insert(any(ParentStudent.class));
    }

    @Test
    @DisplayName("insertStudent仅含主要家长应成功插入")
    void insertStudent_withOnlyPrimaryParent_shouldInsertSuccessfully() {
        InsertParentDTO primaryParentDTO = new InsertParentDTO();
        primaryParentDTO.setUsername("父亲");
        primaryParentDTO.setRelation("父亲");
        primaryParentDTO.setPhone("13800001111");

        InsertStudentDTO dto = new InsertStudentDTO();
        dto.setStudentName("张三");
        dto.setInstitutionId(1L);
        dto.setSex(1L);
        dto.setPrimaryParent(primaryParentDTO);
        dto.setSecondaryParent(null);

        Student mockStudent = new Student();
        mockStudent.setId(100L);
        when(studentConverter.insertStudentDTOToPojo(dto)).thenReturn(mockStudent);
        when(studentMapper.insert(mockStudent)).thenReturn(1);

        Parent mockParent = new Parent();
        mockParent.setParentId(200L);
        when(parentConverter.insertDTOToPojo(primaryParentDTO)).thenReturn(mockParent);
        when(parentMapper.insert(mockParent)).thenReturn(1);

        ResponseDTO<InsertStudentVO> result = studentService.insertStudent(dto);

        assertEquals(Long.valueOf(200L), result.getCode());
        assertEquals(Long.valueOf(100L), result.getData().getStudentId());
        assertTrue(result.getMessage().contains("仅包含主要家长"));

        verify(studentMapper).insert(mockStudent);
        verify(parentMapper).insert(mockParent);
        verify(parentStudentMapper, times(1)).insert(any(ParentStudent.class));
    }

    @Test
    @DisplayName("insertStudent包含主次家长应全部插入成功")
    void insertStudent_withBothParents_shouldInsertBothSuccessfully() {
        InsertParentDTO primaryParentDTO = new InsertParentDTO();
        primaryParentDTO.setUsername("父亲");
        primaryParentDTO.setRelation("父亲");
        primaryParentDTO.setPhone("13800001111");

        InsertParentDTO secondaryParentDTO = new InsertParentDTO();
        secondaryParentDTO.setUsername("母亲");
        secondaryParentDTO.setRelation("母亲");
        secondaryParentDTO.setPhone("13800002222");

        InsertStudentDTO dto = new InsertStudentDTO();
        dto.setStudentName("李四");
        dto.setInstitutionId(1L);
        dto.setSex(0L);
        dto.setPrimaryParent(primaryParentDTO);
        dto.setSecondaryParent(secondaryParentDTO);

        Student mockStudent = new Student();
        mockStudent.setId(101L);
        when(studentConverter.insertStudentDTOToPojo(dto)).thenReturn(mockStudent);
        when(studentMapper.insert(mockStudent)).thenReturn(1);

        Parent mockPrimaryParent = new Parent();
        mockPrimaryParent.setParentId(201L);
        Parent mockSecondaryParent = new Parent();
        mockSecondaryParent.setParentId(202L);
        when(parentConverter.insertDTOToPojo(primaryParentDTO)).thenReturn(mockPrimaryParent);
        when(parentConverter.insertDTOToPojo(secondaryParentDTO)).thenReturn(mockSecondaryParent);

        when(parentMapper.insert(any(Parent.class))).thenReturn(1);

        ResponseDTO<InsertStudentVO> result = studentService.insertStudent(dto);

        assertEquals(Long.valueOf(200L), result.getCode());
        assertEquals(Long.valueOf(101L), result.getData().getStudentId());
        assertTrue(result.getMessage().contains("包含次要家长"));

        verify(parentMapper, times(2)).insert(any(Parent.class));
        verify(parentStudentMapper, times(2)).insert(any(ParentStudent.class));
    }

    @Test
    @DisplayName("updateStudent更新主要家长应正确处理")
    void updateStudent_withPrimaryParent_shouldUpdateCorrectly() {
        UpdateParentDTO primaryParentDTO = new UpdateParentDTO();
        primaryParentDTO.setParentId(200L);
        primaryParentDTO.setUsername("父亲");
        primaryParentDTO.setRelation("父亲");
        primaryParentDTO.setIsPrimary(true);

        UpdateStudentDTO dto = new UpdateStudentDTO();
        dto.setId(100L);
        dto.setStudentName("张三更新");
        dto.setSex(1L);
        dto.setPrimaryParent(primaryParentDTO);
        dto.setSecondaryParent(null);

        Parent mockParent = new Parent();
        mockParent.setParentId(200L);
        when(parentConverter.updateDTOToPojo(primaryParentDTO)).thenReturn(mockParent);
        doReturn(true).when(parentMapper).insertOrUpdate(any(Parent.class));
        when(parentStudentMapper.selectOne(any(LambdaQueryWrapper.class)))
                .thenReturn(new ParentStudent(1L, 200L, "父亲", 100L, true, null));
        when(parentStudentMapper.updateById(any(ParentStudent.class))).thenReturn(1);

        Student mockStudent = new Student();
        mockStudent.setId(100L);
        when(studentConverter.updateStudentDTOToPojo(dto)).thenReturn(mockStudent);
        when(studentMapper.updateById(mockStudent)).thenReturn(1);

        ResponseDTO<?> result = studentService.updateStudent(dto);

        assertEquals(Long.valueOf(200L), result.getCode());
        verify(parentMapper).insertOrUpdate(any(Parent.class));
        verify(parentStudentMapper).updateById(any(ParentStudent.class));
        verify(studentMapper).updateById(any(Student.class));
    }

    @Test
    @DisplayName("getStudent返回null")
    void getStudent_shouldReturnNull() {
        ResponseDTO<QueryStudentVO> result = studentService.getStudent(new QueryStudentDTO());
        assertNull(result);
    }

    @Test
    @DisplayName("getStudentByStudentId查询单个学生应返回带家长信息的结果")
    void getStudentByStudentId_shouldReturnStudentWithParentInfo() {
        QueryStudentDTO dto = new QueryStudentDTO();
        dto.setStudentId(100L);

        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setId(100L);
        studentDTO.setStudentName("张三");
        when(studentMapper.selectByStudentId(dto)).thenReturn(studentDTO);

        StudentVO studentVO = new StudentVO();
        studentVO.setId(100L);
        studentVO.setStudentName("张三");
        when(studentConverter.dtoToVO(studentDTO)).thenReturn(studentVO);

        ParentVO primaryParentVO = new ParentVO();
        primaryParentVO.setIsPrimary(1);
        primaryParentVO.setStudentId(100L);
        when(parentStudentMapper.selectAllBatchParents(eq(List.of(100L))))
                .thenReturn(List.of(primaryParentVO));

        ResponseDTO<QueryStudentVO> result = studentService.getStudentByStudentId(dto);

        assertEquals(Long.valueOf(200L), result.getCode());
        assertNotNull(result.getData());
        assertEquals(1, result.getData().getList().size());
        assertNotNull(result.getData().getList().get(0).getPrimaryParent());
    }

    @Test
    @DisplayName("getStudentByStudentId学生不存在应返回fail")
    void getStudentByStudentId_whenStudentNotExists_shouldReturnFail() {
        QueryStudentDTO dto = new QueryStudentDTO();
        dto.setStudentId(999L);

        when(studentMapper.selectByStudentId(dto)).thenReturn(null);

        ResponseDTO<QueryStudentVO> result = studentService.getStudentByStudentId(dto);

        assertEquals(Long.valueOf(400L), result.getCode());
        assertEquals("学生不存在", result.getMessage());
    }

    @Test
    @DisplayName("getStudentByParentId分页查询应返回含机构信息的结果")
    void getStudentByParentId_shouldReturnStudentsWithInstitutions() {
        QueryStudentDTO dto = new QueryStudentDTO();
        dto.setParentId(200L);
        dto.setCurrentPage(1L);
        dto.setPageSize(10L);

        StudentDTO studentDTO1 = new StudentDTO();
        studentDTO1.setId(1L);
        studentDTO1.setStudentName("张三");
        List<StudentDTO> dtoList = List.of(studentDTO1);

        doAnswer(invocation -> {
            IPage<StudentDTO> page = invocation.getArgument(0);
            page.setRecords(dtoList);
            page.setTotal(1);
            return page;
        }).when(studentMapper).selectStudentByParentId(any(), eq(dto));

        StudentVO studentVO = new StudentVO();
        studentVO.setId(1L);
        studentVO.setStudentName("张三");
        when(studentConverter.dtoListToVOList(dtoList)).thenReturn(List.of(studentVO));

        when(institutionMapper.selectListByStudentId(1L)).thenReturn(Collections.emptyList());

        ResponseDTO<QueryStudentVO> result = studentService.getStudentByParentId(dto);

        assertEquals(Long.valueOf(200L), result.getCode());
        assertEquals(Long.valueOf(1L), result.getData().getTotal());
        assertEquals(1, result.getData().getList().size());
    }

    @Test
    @DisplayName("getStudentByTeacherId分页查询空结果应返回空列表")
    void getStudentByTeacherId_whenNoRecords_shouldReturnEmptyList() {
        QueryStudentDTO dto = new QueryStudentDTO();
        dto.setTeacherId(300L);
        dto.setCurrentPage(1L);
        dto.setPageSize(10L);
        dto.setSex(-1L);

        doAnswer(invocation -> {
            IPage<StudentDTO> page = invocation.getArgument(0);
            page.setRecords(List.of());
            page.setTotal(0);
            return page;
        }).when(studentMapper).selectStudentByTeacherId(any(), eq(dto));

        ResponseDTO<QueryStudentVO> result = studentService.getStudentByTeacherId(dto);

        assertEquals(Long.valueOf(200L), result.getCode());
        assertEquals(Long.valueOf(0L), result.getData().getTotal());
        assertTrue(result.getData().getList().isEmpty());
    }

    @Test
    @DisplayName("getStudentByClassId分页查询应注入家长信息")
    void getStudentByClassId_shouldInjectParentInfo() {
        QueryStudentDTO dto = new QueryStudentDTO();
        dto.setClassId(10L);
        dto.setCurrentPage(1L);
        dto.setPageSize(10L);

        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setId(1L);
        studentDTO.setStudentName("王五");
        List<StudentDTO> dtoList = List.of(studentDTO);

        doAnswer(invocation -> {
            IPage<StudentDTO> page = invocation.getArgument(0);
            page.setRecords(dtoList);
            page.setTotal(1);
            return page;
        }).when(studentMapper).selectStudentByClassId(any(), eq(dto));

        StudentVO studentVO = new StudentVO();
        studentVO.setId(1L);
        studentVO.setStudentName("王五");
        when(studentConverter.dtoToVO(studentDTO)).thenReturn(studentVO);

        ParentVO primaryVO = new ParentVO();
        primaryVO.setIsPrimary(1);
        primaryVO.setStudentId(1L);
        ParentVO secondaryVO = new ParentVO();
        secondaryVO.setIsPrimary(0);
        secondaryVO.setStudentId(1L);
        when(parentStudentMapper.selectAllBatchParents(eq(List.of(1L))))
                .thenReturn(List.of(primaryVO, secondaryVO));

        ResponseDTO<QueryStudentVO> result = studentService.getStudentByClassId(dto);

        assertEquals(Long.valueOf(200L), result.getCode());
        StudentVO resultVO = result.getData().getList().get(0);
        assertNotNull(resultVO.getPrimaryParent());
        assertNotNull(resultVO.getSecondaryParent());
    }
}
