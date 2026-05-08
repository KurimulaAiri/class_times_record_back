package com.shiroko.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shiroko.repository.entity.Course;
import com.shiroko.repository.vo.course.CourseVO;

/**
 * Description: 课程数据库操作接口
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/7 上午0:07
 */
public interface CourseMapper extends BaseMapper<Course> {

    IPage<CourseVO> selectCourseByInstitutionId(IPage<Course> pageParam, Long institutionId);
}




