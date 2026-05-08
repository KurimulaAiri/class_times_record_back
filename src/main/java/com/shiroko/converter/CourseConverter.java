package com.shiroko.converter;

import com.shiroko.repository.entity.Course;
import com.shiroko.repository.vo.course.CourseVO;
import org.mapstruct.Mapper;

/**
 * Description: 课程转换器
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/9 上午1:01
 */
@Mapper(componentModel = "spring")
public interface CourseConverter extends BaseConverter<Course, CourseVO> {


}
