package com.shiroko.converter;

import com.shiroko.repository.entity.CourseRecord;
import com.shiroko.repository.vo.CourseRecordVO;
import com.shiroko.repository.vo.QueryCourseRecordVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Description: TODO
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/2/7 下午9:43
 */
// 声明为MapStruct的映射器，componentModel = "spring" 支持Spring注入
@Mapper(componentModel = "spring")
public interface CourseRecordConverter {
    // 获取转换器实例
    CourseRecordConverter INSTANCE = Mappers.getMapper(CourseRecordConverter.class);

    // 单个对象转换：指定字段映射关系
    // @Mapping(source = "id", target = "userId")          // POJO的id映射到VO的userId
    // @Mapping(source = "username", target = "userName")  // POJO的username映射到VO的userName
    @Mapping(source = "createTime", target = "createTimeStr", qualifiedByName = "dateToString") // 自定义转换规则
    @Mapping(source = "updateTime", target = "updateTimeStr", qualifiedByName = "dateToString") // 自定义转换规则
    @Mapping(source = "courseLastTime", target = "courseLastTimeStr", qualifiedByName = "dateToString") // 自定义转换规则
    // @Mapping(target = "password", ignore = true)        // 忽略密码字段

    CourseRecordVO pojoToVO(CourseRecord pojo);

    // 集合对象转换（MapStruct会自动生成实现）
    List<CourseRecordVO> pojoListToVOList(List<CourseRecord> pojoList);

    // 自定义日期转换方法
    @Named("dateToString")
    default String dateToString(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }
}
