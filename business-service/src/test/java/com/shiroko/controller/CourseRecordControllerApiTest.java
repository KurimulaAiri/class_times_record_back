package com.shiroko.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.dto.clazz.DeductClassDTO;
import com.shiroko.repository.dto.courserecord.*;
import com.shiroko.repository.vo.courserecord.CourseRecordVO;
import com.shiroko.repository.vo.courserecord.DeductCourseRecordVO;
import com.shiroko.repository.vo.courserecord.QueryCourseRecordVO;
import com.shiroko.service.CourseRecordService;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CourseRecordControllerApiTest {

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());
    @Mock
    private CourseRecordService courseRecordService;
    @InjectMocks
    private CourseRecordController courseRecordController;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(courseRecordController).build();
    }

    @Test
    @DisplayName("POST /course_record/add - 娣诲姞璇剧▼璁板綍鎴愬姛")
    void addCourseRecord_shouldReturn200() throws Exception {
        InsertCourseRecordDTO dto = new InsertCourseRecordDTO();
        dto.setStuName("寮犱笁");
        dto.setCourseName("鏁板");
        dto.setCourseTotalTime(20L);
        dto.setCourseRestTime(20L);

        when(courseRecordService.addCourseRecord(any(InsertCourseRecordDTO.class)))
                .thenReturn(ResponseDTO.success("娣诲姞鎴愬姛", new int[]{1, 1}));

        mockMvc.perform(post("/course_record/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        verify(courseRecordService).addCourseRecord(any(InsertCourseRecordDTO.class));
    }

    @Test
    @DisplayName("POST /course_record/add - 缂哄皯蹇呭～瀛楁杩斿洖400")
    void addCourseRecord_missingField_shouldReturn400() throws Exception {
        InsertCourseRecordDTO dto = new InsertCourseRecordDTO();

        mockMvc.perform(post("/course_record/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /course_record/delete - 閫昏緫鍒犻櫎鎴愬姛")
    void deleteCourseRecord_shouldReturn200() throws Exception {
        DeleteCourseRecordDTO dto = new DeleteCourseRecordDTO(50L);

        when(courseRecordService.deleteCourseRecord(any(DeleteCourseRecordDTO.class)))
                .thenReturn(ResponseDTO.success("鍒犻櫎鎴愬姛锛屽奖鍝嶈褰曟暟锛?"));

        mockMvc.perform(post("/course_record/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        verify(courseRecordService).deleteCourseRecord(any(DeleteCourseRecordDTO.class));
    }

    @Test
    @DisplayName("POST /course_record/delete - 缂哄皯ID杩斿洖400")
    void deleteCourseRecord_missingId_shouldReturn400() throws Exception {
        DeleteCourseRecordDTO dto = new DeleteCourseRecordDTO();

        mockMvc.perform(post("/course_record/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /course_record/update - 鏇存柊璇剧▼璁板綍鎴愬姛")
    void updateCourseRecord_shouldReturn200() throws Exception {
        UpdateCourseRecordDTO dto = new UpdateCourseRecordDTO();
        dto.setId(1L);
        dto.setCourseTotalTime(30L);
        dto.setCourseRestTime(15L);
        dto.setCourseStatus(1L);
        dto.setCourseName("鏁板杩涢樁");

        when(courseRecordService.updateCourseRecord(any(UpdateCourseRecordDTO.class)))
                .thenReturn(ResponseDTO.success("鏇存柊鎴愬姛锛屽奖鍝嶈褰曟暟锛?"));

        mockMvc.perform(post("/course_record/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        verify(courseRecordService).updateCourseRecord(any(UpdateCourseRecordDTO.class));
    }

    @Test
    @DisplayName("POST /course_record/deduct_by_student_id - 鎵ｈ鎴愬姛")
    void deductByStudentId_shouldReturn200() throws Exception {
        DeductClassDTO deductClass = new DeductClassDTO(1L, 10L, 3, "鎵ｈ");
        DeductCourseRecordDTO dto = new DeductCourseRecordDTO(100L, "娴嬭瘯鎵ｈ", List.of(deductClass));

        when(courseRecordService.deductByStudentId(any(DeductCourseRecordDTO.class)))
                .thenReturn(ResponseDTO.success(new DeductCourseRecordVO(1)));

        mockMvc.perform(post("/course_record/deduct_by_student_id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.res").value(1));

        verify(courseRecordService).deductByStudentId(any(DeductCourseRecordDTO.class));
    }

    @Test
    @DisplayName("POST /course_record/get - 鍒嗛〉鏌ヨ璇剧▼璁板綍")
    void getCourseRecords_shouldReturnPagedData() throws Exception {
        QueryCourseRecordDTO dto = new QueryCourseRecordDTO();
        dto.setCurrentPage(1L);
        dto.setPageSize(10L);

        CourseRecordVO vo = new CourseRecordVO();
        vo.setId(1L);
        vo.setStuName("寮犱笁");
        vo.setCourseName("鏁板");
        vo.setCourseTotalTime(20L);
        vo.setCourseRestTime(15L);

        QueryCourseRecordVO queryVO = new QueryCourseRecordVO(List.of(vo), 1L);

        when(courseRecordService.getCourseRecords(any(QueryCourseRecordDTO.class)))
                .thenReturn(ResponseDTO.success(queryVO));

        mockMvc.perform(post("/course_record/get")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.total").value(1))
                .andExpect(jsonPath("$.data.courseRecords[0].stuName").value("寮犱笁"))
                .andExpect(jsonPath("$.data.courseRecords[0].courseName").value("鏁板"));
    }

    @Test
    @DisplayName("POST /course_record/new_get - 鏂版柟寮忓垎椤垫煡璇?)
    void newGetCourseRecords_shouldReturnPagedData() throws Exception {
        QueryCourseRecordDTO dto = new QueryCourseRecordDTO();
        dto.setCurrentPage(1L);
        dto.setPageSize(10L);

        CourseRecordVO vo = new CourseRecordVO();
        vo.setId(1L);
        vo.setStuName("鏉庡洓");

        QueryCourseRecordVO queryVO = new QueryCourseRecordVO(List.of(vo), 1L);

        when(courseRecordService.newGetCourseRecords(any(QueryCourseRecordDTO.class)))
                .thenReturn(ResponseDTO.success(queryVO));

        mockMvc.perform(post("/course_record/new_get")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.total").value(1));
    }
}
