package com.shiroko.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shiroko.repository.dto.course.QueryCourseDTO;
import com.shiroko.repository.entity.Course;
import com.shiroko.repository.vo.course.CourseVO;
import org.apache.ibatis.annotations.Param;

/**
 * Description: 课程数据库操作接口
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/7 上午0:07
 */
public interface CourseMapper extends BaseMapper<Course> {

    IPage<CourseVO> selectCourseByInstitutionId(IPage<Course> pageParam, @Param("dto") QueryCourseDTO dto);

    IPage<CourseVO> selectCourseByStudentId(IPage<Course> pageParam, @Param("dto") QueryCourseDTO dto);
}




