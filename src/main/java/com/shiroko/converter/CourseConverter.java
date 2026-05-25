package com.shiroko.converter;

import com.shiroko.repository.dto.course.InsertCourseDTO;
import com.shiroko.repository.entity.Course;
import com.shiroko.repository.vo.course.CourseVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Description: 课程转换器
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/9 上午1:01
 */
@Mapper(componentModel = "spring")
public interface CourseConverter extends BaseConverter<Course, CourseVO> {

    @Mapping(target = "isAvailable", source = "isAvailable")
    Course insertDtoToPojo(InsertCourseDTO insertCourseDTO);

}
