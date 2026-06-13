package com.shiroko.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shiroko.repository.dto.record.QueryRecordDTO;
import com.shiroko.repository.dto.record.RecordDTO;
import com.shiroko.repository.entity.Record;
import org.apache.ibatis.annotations.Param;

/**
 * Description: 记录Mapper
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/3/22 下午22:50
 */
public interface RecordMapper extends BaseMapper<Record> {

    IPage<RecordDTO> selectRecords(IPage<RecordDTO> recordPage, @Param("dto") QueryRecordDTO dto);
}
