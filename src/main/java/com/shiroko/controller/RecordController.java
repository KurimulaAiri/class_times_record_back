package com.shiroko.controller;

import com.shiroko.repository.dto.InsertRecordDTO;
import com.shiroko.repository.dto.InsertRecordsDTO;
import com.shiroko.repository.dto.QueryRecordDTO;
import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.vo.QueryRecordVO;
import com.shiroko.service.RecordService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description: 课程记录控制器
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/3/22 下午10:52
 */
@RestController
@RequestMapping("/record")
public class RecordController {
    private final RecordService recordService;

    @Autowired
    public RecordController(RecordService recordService) {
        this.recordService = recordService;
    }

    @PostMapping("/get")
    public ResponseDTO<QueryRecordVO> getRecord(@Valid @RequestBody QueryRecordDTO queryRecordDTO) {
        return recordService.getRecord(queryRecordDTO);
    }

    @PostMapping("/add")
    public ResponseDTO<Object> insertRecord(@Valid @RequestBody InsertRecordDTO insertRecordDTO) {
        return recordService.insertRecord(insertRecordDTO);
    }

    @PostMapping("/add_all")
    public ResponseDTO<Object> insertRecords(@Valid @RequestBody InsertRecordsDTO insertRecordsDTO) {
        return recordService.insertRecords(insertRecordsDTO);
    }
}
