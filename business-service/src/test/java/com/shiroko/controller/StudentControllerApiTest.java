package com.shiroko.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.dto.parent.InsertParentDTO;
import com.shiroko.repository.dto.parent.UpdateParentDTO;
import com.shiroko.repository.dto.student.InsertStudentDTO;
import com.shiroko.repository.dto.student.QueryStudentDTO;
import com.shiroko.repository.dto.student.UpdateStudentDTO;
import com.shiroko.repository.vo.parent.ParentVO;
import com.shiroko.repository.vo.student.InsertStudentVO;
import com.shiroko.repository.vo.student.QueryStudentVO;
import com.shiroko.repository.vo.student.StudentVO;
import com.shiroko.repository.vo.student.UpdateStudentVO;
import com.shiroko.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class StudentControllerApiTest {

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());
    @Mock
    private StudentService studentService;
    @InjectMocks
    private StudentController studentController;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();
    }

    @Test
    @DisplayName("POST /student/insert - 创建学生成功")
    void insertStudent_shouldReturn200() throws Exception {
        InsertParentDTO parentDTO = new InsertParentDTO();
        parentDTO.setUsername("父亲");
        parentDTO.setRelation("父亲");
        parentDTO.setPhone("13800001111");

        InsertStudentDTO dto = new InsertStudentDTO();
        dto.setStudentName("张三");
        dto.setInstitutionId(1L);
        dto.setSex(1L);
        dto.setPrimaryParent(parentDTO);
        dto.setSecondaryParent(null);

        when(studentService.insertStudent(any(InsertStudentDTO.class)))
                .thenReturn(ResponseDTO.success("插入学生成功(仅包含主要家长)", new InsertStudentVO(100L)));

        mockMvc.perform(post("/student/insert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.studentId").value(100));

        verify(studentService).insertStudent(any(InsertStudentDTO.class));
    }

    @Test
    @DisplayName("POST /student/update - 更新学生成功")
    void updateStudent_shouldReturn200() throws Exception {
        UpdateParentDTO parentDTO = new UpdateParentDTO();
        parentDTO.setParentId(200L);
        parentDTO.setUsername("父亲更新");
        parentDTO.setRelation("父亲");
        parentDTO.setIsPrimary(true);

        UpdateStudentDTO dto = new UpdateStudentDTO();
        dto.setId(100L);
        dto.setStudentName("张三更新");
        dto.setSex(1L);
        dto.setPrimaryParent(parentDTO);

        when(studentService.updateStudent(any(UpdateStudentDTO.class)))
                .thenReturn(ResponseDTO.success("更新成功", new UpdateStudentVO(100L)));

        mockMvc.perform(post("/student/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        verify(studentService).updateStudent(any(UpdateStudentDTO.class));
    }

    @Test
    @DisplayName("POST /student/get_by_student_id - 按学生ID查询成功")
    void getStudentById_shouldReturn200() throws Exception {
        QueryStudentDTO dto = new QueryStudentDTO();
        dto.setStudentId(100L);

        StudentVO vo = new StudentVO();
        vo.setId(100L);
        vo.setStudentName("张三");

        QueryStudentVO queryVO = new QueryStudentVO(Collections.singletonList(vo), 1L);

        when(studentService.getStudentByStudentId(any(QueryStudentDTO.class)))
                .thenReturn(ResponseDTO.success(queryVO));

        mockMvc.perform(post("/student/get_by_student_id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.list[0].studentName").value("张三"))
                .andExpect(jsonPath("$.data.total").value(1));
    }

    @Test
    @DisplayName("POST /student/get_by_student_id - 缺少ID返回400")
    void getStudentById_missingId_shouldReturn400() throws Exception {
        QueryStudentDTO dto = new QueryStudentDTO();

        mockMvc.perform(post("/student/get_by_student_id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /student/get_by_parent_id - 按家长ID分页查询")
    void getStudentByParentId_shouldReturn200() throws Exception {
        QueryStudentDTO dto = new QueryStudentDTO();
        dto.setParentId(200L);
        dto.setCurrentPage(1L);
        dto.setPageSize(10L);

        StudentVO vo = new StudentVO();
        vo.setId(1L);
        vo.setStudentName("张三");

        QueryStudentVO queryVO = new QueryStudentVO(List.of(vo), 1L);

        when(studentService.getStudentByParentId(any(QueryStudentDTO.class)))
                .thenReturn(ResponseDTO.success(queryVO));

        mockMvc.perform(post("/student/get_by_parent_id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.list[0].studentName").value("张三"));
    }

    @Test
    @DisplayName("POST /student/get_by_teacher_id - 按教师ID分页查询")
    void getStudentByTeacherId_shouldReturn200() throws Exception {
        QueryStudentDTO dto = new QueryStudentDTO();
        dto.setTeacherId(300L);
        dto.setCurrentPage(1L);
        dto.setPageSize(10L);

        StudentVO vo = new StudentVO();
        vo.setId(2L);
        vo.setStudentName("李四");

        QueryStudentVO queryVO = new QueryStudentVO(List.of(vo), 1L);

        when(studentService.getStudentByTeacherId(any(QueryStudentDTO.class)))
                .thenReturn(ResponseDTO.success(queryVO));

        mockMvc.perform(post("/student/get_by_teacher_id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.list[0].studentName").value("李四"));
    }

    @Test
    @DisplayName("POST /student/get_by_class_id - 按班级ID分页查询(含家长信息)")
    void getStudentByClassId_shouldReturn200() throws Exception {
        QueryStudentDTO dto = new QueryStudentDTO();
        dto.setClassId(10L);
        dto.setCurrentPage(1L);
        dto.setPageSize(10L);

        StudentVO vo = new StudentVO();
        vo.setId(1L);
        vo.setStudentName("王五");

        ParentVO primaryParent = new ParentVO();
        primaryParent.setIsPrimary(1);
        primaryParent.setStudentId(1L);
        primaryParent.setUsername("父亲");
        vo.setPrimaryParent(primaryParent);

        QueryStudentVO queryVO = new QueryStudentVO(List.of(vo), 1L);

        when(studentService.getStudentByClassId(any(QueryStudentDTO.class)))
                .thenReturn(ResponseDTO.success(queryVO));

        mockMvc.perform(post("/student/get_by_class_id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.list[0].primaryParent.username").value("父亲"));
    }

    @Test
    @DisplayName("POST /student/get_by_institution_id - 按机构ID分页查询")
    void getStudentByInstitutionId_shouldReturn200() throws Exception {
        QueryStudentDTO dto = new QueryStudentDTO();
        dto.setInstitutionId(1L);
        dto.setCurrentPage(1L);
        dto.setPageSize(10L);

        StudentVO vo = new StudentVO();
        vo.setId(1L);
        vo.setStudentName("赵六");

        QueryStudentVO queryVO = new QueryStudentVO(List.of(vo), 1L);

        when(studentService.getStudentByInstitutionId(any(QueryStudentDTO.class)))
                .thenReturn(ResponseDTO.success(queryVO));

        mockMvc.perform(post("/student/get_by_institution_id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.list[0].studentName").value("赵六"));
    }
}