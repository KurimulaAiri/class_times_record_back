package com.shiroko.converter;

import com.shiroko.repository.entity.ClassSchedule;
import com.shiroko.repository.vo.classschedule.ClassScheduleVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Description: 班级排课转换器
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/16 下午10:36
 */
@Mapper(componentModel = "spring")
public interface ClassScheduleConverter extends BaseConverter<ClassSchedule, ClassScheduleVO> {

    @Mapping(target = "startDateStr", source = "startDate", qualifiedByName = "dateToString")
    @Mapping(target = "endDateStr", source = "endDate", qualifiedByName = "dateToString")
    @Mapping(target = "startTimeStr", source = "startTime", qualifiedByName = "timeToString")
    @Mapping(target = "endTimeStr", source = "endTime", qualifiedByName = "timeToString")
    @Mapping(target = "createTimeStr", source = "createTime", qualifiedByName = "dateTimeToString")
    @Mapping(target = "updateTimeStr", source = "updateTime", qualifiedByName = "dateTimeToString")
    @Override
    ClassScheduleVO pojoToVO(ClassSchedule pojo);

}
