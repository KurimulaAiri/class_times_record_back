package com.shiroko.converter;

import com.shiroko.repository.dto.record.InsertRecordDTO;
import com.shiroko.repository.vo.record.RecordVO;
import com.shiroko.repository.entity.Record;
import org.mapstruct.Mapper;

/**
 * Description: 课程记录转换器
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/3/22 下午11:00
 */
@Mapper(componentModel = "spring")
public interface RecordConverter extends BaseConverter<Record, RecordVO> {

    Record insertDtoToPojo(InsertRecordDTO insertRecordDTO);

}
