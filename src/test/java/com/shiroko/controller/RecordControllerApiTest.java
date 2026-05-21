package com.shiroko.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.dto.record.InsertRecordDTO;
import com.shiroko.repository.dto.record.InsertRecordsDTO;
import com.shiroko.repository.dto.record.QueryRecordDTO;
import com.shiroko.repository.vo.record.QueryRecordVO;
import com.shiroko.repository.vo.record.RecordVO;
import com.shiroko.service.RecordService;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class RecordControllerApiTest {

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());
    @Mock
    private RecordService recordService;
    @InjectMocks
    private RecordController recordController;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(recordController).build();
    }

    @Test
    @DisplayName("POST /record/add - 插入单条记录成功")
    void insertRecord_shouldReturn200() throws Exception {
        InsertRecordDTO dto = new InsertRecordDTO();
        dto.setCourseRecordId(1L);
        dto.setRecordTime(LocalDateTime.of(2026, 3, 22, 10, 0, 0));
        dto.setRecordType(1L);
        dto.setRecordRemark("测试备注");
        dto.setRecordChange(2L);

        when(recordService.insertRecord(any(InsertRecordDTO.class)))
                .thenReturn(ResponseDTO.success("插入成功，影响行数为1"));

        mockMvc.perform(post("/record/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("success"));

        verify(recordService).insertRecord(any(InsertRecordDTO.class));
    }

    @Test
    @DisplayName("POST /record/add_all - 批量插入记录成功")
    void insertRecords_shouldReturn200() throws Exception {
        InsertRecordsDTO dto = new InsertRecordsDTO();
        dto.setCourseRecordIdList(new Long[]{1L, 2L});
        dto.setRecordType(1L);
        dto.setRecordChange(3L);
        dto.setRecordTime(LocalDateTime.of(2026, 3, 22, 10, 0, 0));

        when(recordService.insertRecords(any(InsertRecordsDTO.class)))
                .thenReturn(ResponseDTO.success("插入成功，影响行数为2"));

        mockMvc.perform(post("/record/add_all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        verify(recordService).insertRecords(any(InsertRecordsDTO.class));
    }

    @Test
    @DisplayName("POST /record/get - 分页查询记录成功")
    void getRecord_shouldReturnPagedData() throws Exception {
        QueryRecordDTO dto = new QueryRecordDTO();
        dto.setCourseRecordId(1L);
        dto.setCurrentPage(1);
        dto.setPageSize(10);

        RecordVO vo = new RecordVO();
        vo.setId(1L);
        vo.setCourseRecordId(1L);
        vo.setRecordType(1L);
        vo.setRecordChange(2L);

        QueryRecordVO queryVO = new QueryRecordVO()
                .setRecords(List.of(vo))
                .setTotal(1L);

        when(recordService.getRecord(any(QueryRecordDTO.class)))
                .thenReturn(ResponseDTO.success(queryVO));

        mockMvc.perform(post("/record/get")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.total").value(1))
                .andExpect(jsonPath("$.data.records[0].id").value(1))
                .andExpect(jsonPath("$.data.records[0].recordType").value(1));

        verify(recordService).getRecord(any(QueryRecordDTO.class));
    }

    @Test
    @DisplayName("POST /record/get - 查询无记录返回空列表")
    void getRecord_noResults_shouldReturnEmptyList() throws Exception {
        QueryRecordDTO dto = new QueryRecordDTO();
        dto.setCourseRecordId(999L);
        dto.setCurrentPage(1);
        dto.setPageSize(10);

        QueryRecordVO queryVO = new QueryRecordVO()
                .setRecords(List.of())
                .setTotal(0L);

        when(recordService.getRecord(any(QueryRecordDTO.class)))
                .thenReturn(ResponseDTO.success(queryVO));

        mockMvc.perform(post("/record/get")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.total").value(0))
                .andExpect(jsonPath("$.data.records").isArray())
                .andExpect(jsonPath("$.data.records").isEmpty());
    }
}
