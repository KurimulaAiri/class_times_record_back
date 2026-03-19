package com.shiroko.converter;

import com.shiroko.repository.entity.CourseRecord;
import com.shiroko.repository.vo.CourseRecordVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Description: TODO
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/2/7 下午9:43
 */
// 声明为MapStruct的映射器，componentModel = "spring" 支持Spring注入
@Mapper(componentModel = "spring")
public interface CourseRecordConverter extends BaseConverter<CourseRecord, CourseRecordVO> {


    @Mapping(source = "createTime", target = "createTimeStr", qualifiedByName = "dateToString") // 自定义转换规则
    @Mapping(source = "updateTime", target = "updateTimeStr", qualifiedByName = "dateToString") // 自定义转换规则
    @Mapping(source = "courseLastTime", target = "courseLastTimeStr", qualifiedByName = "dateToString") // 自定义转换规则
    @Override
    CourseRecordVO pojoToVO(CourseRecord pojo);
}
