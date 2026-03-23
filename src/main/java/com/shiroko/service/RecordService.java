package com.shiroko.service;

import com.shiroko.repository.dto.InsertRecordDTO;
import com.shiroko.repository.dto.InsertRecordsDTO;
import com.shiroko.repository.dto.QueryRecordDTO;
import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.entity.Record;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shiroko.repository.vo.QueryRecordVO;

/**
 * Description: 记录服务接口
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/3/22 下午22:50
 */
public interface RecordService extends IService<Record> {

    ResponseDTO<Object> insertRecord(InsertRecordDTO insertRecordDTO);

    ResponseDTO<Object> insertRecords(InsertRecordsDTO insertRecordsDTO);

    ResponseDTO<QueryRecordVO> getRecord(QueryRecordDTO queryRecordDTO);
}
