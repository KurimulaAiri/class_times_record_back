package com.shiroko.converter;

import com.shiroko.annotation.BaseDateTimeToString;
import com.shiroko.annotation.BaseDateToString;
import com.shiroko.annotation.BaseTimeToString;
import com.shiroko.repository.dto.classschedule.ClassScheduleDTO;
import com.shiroko.repository.dto.classschedule.UpdateClassScheduleDTO;
import com.shiroko.repository.entity.ClassSchedule;
import com.shiroko.repository.vo.classschedule.ClassScheduleVO;
import com.shiroko.util.DateTransformUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Description: 班级排课转换器
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/16 下午10:36
 */
@Mapper(componentModel = "spring", uses = {DateTransformUtils.class})
public interface ClassScheduleConverter extends BaseConverter<ClassSchedule, ClassScheduleVO> {

    @Mapping(target = "startDateStr", source = "startDate", qualifiedBy = BaseDateToString.class)
    @Mapping(target = "endDateStr", source = "endDate", qualifiedBy = BaseDateToString.class)
    @Mapping(target = "startTimeStr", source = "startTime", qualifiedBy = BaseTimeToString.class)
    @Mapping(target = "endTimeStr", source = "endTime", qualifiedBy = BaseTimeToString.class)
    @Mapping(target = "createTimeStr", source = "createTime", qualifiedBy = BaseDateTimeToString.class)
    @Mapping(target = "updateTimeStr", source = "updateTime", qualifiedBy = BaseDateTimeToString.class)
    @Override
    ClassScheduleVO pojoToVO(ClassSchedule pojo);

    @Mapping(target = "startDateStr", source = "startDate", qualifiedBy = BaseDateToString.class)
    @Mapping(target = "endDateStr", source = "endDate", qualifiedBy = BaseDateToString.class)
    @Mapping(target = "startTimeStr", source = "startTime", qualifiedBy = BaseTimeToString.class)
    @Mapping(target = "endTimeStr", source = "endTime", qualifiedBy = BaseTimeToString.class)
    @Mapping(target = "createTimeStr", source = "createTime", qualifiedBy = BaseDateTimeToString.class)
    @Mapping(target = "updateTimeStr", source = "updateTime", qualifiedBy = BaseDateTimeToString.class)
    ClassScheduleVO dtoToVO(ClassScheduleDTO dto);

    List<ClassScheduleVO> dtoListToVOList(List<ClassScheduleDTO> dtoList);

    ClassSchedule updateDtoToPojo(UpdateClassScheduleDTO dto);

}
