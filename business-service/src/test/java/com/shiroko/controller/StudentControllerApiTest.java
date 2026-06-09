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
    @DisplayName("POST /student/insert - 鍒涘缓瀛︾敓鎴愬姛")
    void insertStudent_shouldReturn200() throws Exception {
        InsertParentDTO parentDTO = new InsertParentDTO();
        parentDTO.setUsername("鐖朵翰");
        parentDTO.setRelation("鐖朵翰");
        parentDTO.setPhone("13800001111");

        InsertStudentDTO dto = new InsertStudentDTO();
        dto.setStudentName("寮犱笁");
        dto.setInstitutionId(1L);
        dto.setSex(1L);
        dto.setPrimaryParent(parentDTO);
        dto.setSecondaryParent(null);

        when(studentService.insertStudent(any(InsertStudentDTO.class)))
                .thenReturn(ResponseDTO.success("鎻掑叆瀛︾敓鎴愬姛(浠呭寘鍚富瑕佸闀?", new InsertStudentVO(100L)));

        mockMvc.perform(post("/student/insert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.studentId").value(100));

        verify(studentService).insertStudent(any(InsertStudentDTO.class));
    }

    @Test
    @DisplayName("POST /student/update - 鏇存柊瀛︾敓鎴愬姛")
    void updateStudent_shouldReturn200() throws Exception {
        UpdateParentDTO parentDTO = new UpdateParentDTO();
        parentDTO.setParentId(200L);
        parentDTO.setUsername("鐖朵翰鏇存柊");
        parentDTO.setRelation("鐖朵翰");
        parentDTO.setIsPrimary(true);

        UpdateStudentDTO dto = new UpdateStudentDTO();
        dto.setId(100L);
        dto.setStudentName("寮犱笁鏇存柊");
        dto.setSex(1L);
        dto.setPrimaryParent(parentDTO);

        when(studentService.updateStudent(any(UpdateStudentDTO.class)))
                .thenReturn(ResponseDTO.success("鏇存柊鎴愬姛", new UpdateStudentVO(100L)));

        mockMvc.perform(post("/student/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        verify(studentService).updateStudent(any(UpdateStudentDTO.class));
    }

    @Test
    @DisplayName("POST /student/get_by_student_id - 鎸夊鐢烮D鏌ヨ鎴愬姛")
    void getStudentById_shouldReturn200() throws Exception {
        QueryStudentDTO dto = new QueryStudentDTO();
        dto.setStudentId(100L);

        StudentVO vo = new StudentVO();
        vo.setId(100L);
        vo.setStudentName("寮犱笁");

        QueryStudentVO queryVO = new QueryStudentVO(Collections.singletonList(vo), 1L);

        when(studentService.getStudentByStudentId(any(QueryStudentDTO.class)))
                .thenReturn(ResponseDTO.success(queryVO));

        mockMvc.perform(post("/student/get_by_student_id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.list[0].studentName").value("寮犱笁"))
                .andExpect(jsonPath("$.data.total").value(1));
    }

    @Test
    @DisplayName("POST /student/get_by_student_id - 缂哄皯ID杩斿洖400")
    void getStudentById_missingId_shouldReturn400() throws Exception {
        QueryStudentDTO dto = new QueryStudentDTO();

        mockMvc.perform(post("/student/get_by_student_id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /student/get_by_parent_id - 鎸夊闀縄D鍒嗛〉鏌ヨ")
    void getStudentByParentId_shouldReturn200() throws Exception {
        QueryStudentDTO dto = new QueryStudentDTO();
        dto.setParentId(200L);
        dto.setCurrentPage(1L);
        dto.setPageSize(10L);

        StudentVO vo = new StudentVO();
        vo.setId(1L);
        vo.setStudentName("寮犱笁");

        QueryStudentVO queryVO = new QueryStudentVO(List.of(vo), 1L);

        when(studentService.getStudentByParentId(any(QueryStudentDTO.class)))
                .thenReturn(ResponseDTO.success(queryVO));

        mockMvc.perform(post("/student/get_by_parent_id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.list[0].studentName").value("寮犱笁"));
    }

    @Test
    @DisplayName("POST /student/get_by_teacher_id - 鎸夋暀甯圛D鍒嗛〉鏌ヨ")
    void getStudentByTeacherId_shouldReturn200() throws Exception {
        QueryStudentDTO dto = new QueryStudentDTO();
        dto.setTeacherId(300L);
        dto.setCurrentPage(1L);
        dto.setPageSize(10L);

        StudentVO vo = new StudentVO();
        vo.setId(2L);
        vo.setStudentName("鏉庡洓");

        QueryStudentVO queryVO = new QueryStudentVO(List.of(vo), 1L);

        when(studentService.getStudentByTeacherId(any(QueryStudentDTO.class)))
                .thenReturn(ResponseDTO.success(queryVO));

        mockMvc.perform(post("/student/get_by_teacher_id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.list[0].studentName").value("鏉庡洓"));
    }

    @Test
    @DisplayName("POST /student/get_by_class_id - 鎸夌彮绾D鍒嗛〉鏌ヨ锛堝惈瀹堕暱淇℃伅锛?)
    void getStudentByClassId_shouldReturn200() throws Exception {
        QueryStudentDTO dto = new QueryStudentDTO();
        dto.setClassId(10L);
        dto.setCurrentPage(1L);
        dto.setPageSize(10L);

        StudentVO vo = new StudentVO();
        vo.setId(1L);
        vo.setStudentName("鐜嬩簲");

        ParentVO primaryParent = new ParentVO();
        primaryParent.setIsPrimary(1);
        primaryParent.setStudentId(1L);
        primaryParent.setUsername("鐖朵翰");
        vo.setPrimaryParent(primaryParent);

        QueryStudentVO queryVO = new QueryStudentVO(List.of(vo), 1L);

        when(studentService.getStudentByClassId(any(QueryStudentDTO.class)))
                .thenReturn(ResponseDTO.success(queryVO));

        mockMvc.perform(post("/student/get_by_class_id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.list[0].primaryParent.username").value("鐖朵翰"));
    }

    @Test
    @DisplayName("POST /student/get_by_institution_id - 鎸夋満鏋処D鍒嗛〉鏌ヨ")
    void getStudentByInstitutionId_shouldReturn200() throws Exception {
        QueryStudentDTO dto = new QueryStudentDTO();
        dto.setInstitutionId(1L);
        dto.setCurrentPage(1L);
        dto.setPageSize(10L);

        StudentVO vo = new StudentVO();
        vo.setId(1L);
        vo.setStudentName("璧靛叚");

        QueryStudentVO queryVO = new QueryStudentVO(List.of(vo), 1L);

        when(studentService.getStudentByInstitutionId(any(QueryStudentDTO.class)))
                .thenReturn(ResponseDTO.success(queryVO));

        mockMvc.perform(post("/student/get_by_institution_id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.list[0].studentName").value("璧靛叚"));
    }
}
