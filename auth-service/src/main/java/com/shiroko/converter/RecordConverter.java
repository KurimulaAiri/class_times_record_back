package com.shiroko.converter;

import com.shiroko.annotation.BaseDateTimeToString;
import com.shiroko.repository.dto.record.InsertRecordDTO;
import com.shiroko.repository.dto.record.RecordDTO;
import com.shiroko.repository.entity.Record;
import com.shiroko.repository.vo.record.RecordVO;
import com.shiroko.util.DateTransformUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Description: 课程记录转换器
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/3/22 下午11:00
 */
@Mapper(componentModel = "spring", uses = {CourseRecordConverter.class, DateTransformUtils.class})
public interface RecordConverter extends BaseConverter<Record, RecordVO> {

    Record insertDtoToPojo(InsertRecordDTO insertRecordDTO);

    @Mapping(source = "createTime", target = "createTimeStr", qualifiedBy = BaseDateTimeToString.class) // 自定义转换规则
    @Mapping(source = "recordTime", target = "recordTimeStr", qualifiedBy = BaseDateTimeToString.class) // 自定义转换规则
    @Mapping(source = "updateTime", target = "updateTimeStr", qualifiedBy = BaseDateTimeToString.class)
        // 自定义转换规则
    RecordVO dtoToVo(RecordDTO recordDTO);

    List<RecordVO> dtoListToVoList(List<RecordDTO> records);

}
