package com.shiroko.controller;

import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.dto.course.QueryCourseDTO;
import com.shiroko.repository.vo.course.QueryCourseVO;
import com.shiroko.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description: 课程控制器
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/9 上午12:34
 */
@RestController
@RequestMapping("/course")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @RequestMapping("/get_course_by_institution_id")
    public ResponseDTO<QueryCourseVO> getCourseByInstitutionId(@RequestBody QueryCourseDTO queryCourseDTO) {
        return courseService.getCourseByInstitutionId(queryCourseDTO);
    }


}
