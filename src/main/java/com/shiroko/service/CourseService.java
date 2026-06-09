package com.shiroko.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.dto.course.InsertCourseDTO;
import com.shiroko.repository.dto.course.QueryCourseDTO;
import com.shiroko.repository.dto.course.UpdateCourseDTO;
import com.shiroko.repository.entity.Course;
import com.shiroko.repository.vo.course.InsertCourseVO;
import com.shiroko.repository.vo.course.QueryCourseVO;
import com.shiroko.repository.vo.course.UpdateCourseVO;

/**
 * Description: 课程服务接口
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/7 上午0:07
 */
public interface CourseService extends IService<Course> {

    ResponseDTO<QueryCourseVO> getCourseByInstitutionId(QueryCourseDTO queryCourseDTO);

    ResponseDTO<InsertCourseVO> addCourse(InsertCourseDTO insertCourseDTO);

    ResponseDTO<QueryCourseVO> getCourseByStudentId(QueryCourseDTO queryCourseDTO);

    ResponseDTO<UpdateCourseVO> updateCourseById(UpdateCourseDTO updateCourseDTO);
}
