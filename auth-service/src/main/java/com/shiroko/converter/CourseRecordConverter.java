package com.shiroko.converter;

import com.shiroko.annotation.BaseDateTimeToString;
import com.shiroko.repository.dto.courserecord.CourseRecordDTO;
import com.shiroko.repository.dto.courserecord.InsertCourseRecordDTO;
import com.shiroko.repository.dto.courserecord.UpdateCourseRecordDTO;
import com.shiroko.repository.entity.CourseRecord;
import com.shiroko.repository.vo.courserecord.CourseRecordVO;
import com.shiroko.util.DateTransformUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Description: 课程记录转换器
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/2/7 下午9:43
 */
// 声明为MapStruct的映射器，componentModel = "spring" 支持Spring注入
@Mapper(componentModel = "spring", uses = {DateTransformUtils.class})
public interface CourseRecordConverter extends BaseConverter<CourseRecord, CourseRecordVO> {

    @Mapping(source = "expireTime", target = "expireTimeStr", qualifiedBy = BaseDateTimeToString.class)
    @Mapping(source = "createTime", target = "createTimeStr", qualifiedBy = BaseDateTimeToString.class) // 自定义转换规则
    @Mapping(source = "updateTime", target = "updateTimeStr", qualifiedBy = BaseDateTimeToString.class) // 自定义转换规则
    @Mapping(source = "courseLastTime", target = "courseLastTimeStr", qualifiedBy = BaseDateTimeToString.class)
    // 自定义转换规则
    @Override
    CourseRecordVO pojoToVO(CourseRecord pojo);

    @Mapping(source = "expireTime", target = "expireTimeStr", qualifiedBy = BaseDateTimeToString.class)
    @Mapping(source = "createTime", target = "createTimeStr", qualifiedBy = BaseDateTimeToString.class) // 自定义转换规则
    @Mapping(source = "updateTime", target = "updateTimeStr", qualifiedBy = BaseDateTimeToString.class) // 自定义转换规则
    @Mapping(source = "courseLastTime", target = "courseLastTimeStr", qualifiedBy = BaseDateTimeToString.class)
        // 自定义转换规则
    CourseRecordVO dtoToVO(CourseRecordDTO dto);

    List<CourseRecordVO> dtoListToVOList(List<CourseRecordDTO> dtoList);

    CourseRecord insertDtoToPojo(InsertCourseRecordDTO insertCourseRecordDTO);

    @Mapping(source = "id", target = "id")
    CourseRecord updateDtoToPojo(UpdateCourseRecordDTO updateCourseRecordDTO);
}
