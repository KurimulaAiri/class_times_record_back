package com.shiroko.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiroko.converter.CourseConverter;
import com.shiroko.mapper.CourseMapper;
import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.dto.course.CourseDTO;
import com.shiroko.repository.dto.course.InsertCourseDTO;
import com.shiroko.repository.dto.course.QueryCourseDTO;
import com.shiroko.repository.dto.course.UpdateCourseDTO;
import com.shiroko.repository.entity.Course;
import com.shiroko.repository.vo.course.CourseVO;
import com.shiroko.repository.vo.course.InsertCourseVO;
import com.shiroko.repository.vo.course.QueryCourseVO;
import com.shiroko.repository.vo.course.UpdateCourseVO;
import com.shiroko.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description: 课程服务接口实现
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/7 上午0:07
 */
@Service
@RequiredArgsConstructor
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    private final CourseMapper courseMapper;

    private final CourseConverter courseConverter;

    @Override
    public ResponseDTO<QueryCourseVO> getCourseByInstitutionId(QueryCourseDTO queryCourseDTO) {

        IPage<Course> pageParam = new Page<>(queryCourseDTO.getCurrentPage(), queryCourseDTO.getPageSize());

        IPage<CourseDTO> iPage = courseMapper.selectCourseByInstitutionId(pageParam, queryCourseDTO);

        QueryCourseVO queryCourseVO = convertToQueryCourseVO(iPage);
        return ResponseDTO.success(queryCourseVO);
    }

    @Override
    public ResponseDTO<InsertCourseVO> addCourse(InsertCourseDTO insertCourseDTO) {
        Course course = courseConverter.insertDtoToPojo(insertCourseDTO);
        courseMapper.insert(course);
        return ResponseDTO.success(new InsertCourseVO(course.getId()));
    }

    @Override
    public ResponseDTO<QueryCourseVO> getCourseByStudentId(QueryCourseDTO queryCourseDTO) {
        IPage<Course> pageParam = new Page<>(queryCourseDTO.getCurrentPage(), queryCourseDTO.getPageSize());

        IPage<CourseDTO> iPage = courseMapper.selectCourseByStudentId(pageParam, queryCourseDTO);

        QueryCourseVO queryCourseVO = convertToQueryCourseVO(iPage);
        return ResponseDTO.success(queryCourseVO);
    }

    @Override
    public ResponseDTO<UpdateCourseVO> updateCourseById(UpdateCourseDTO updateCourseDTO) {
        return null;
    }

    private QueryCourseVO convertToQueryCourseVO(IPage<CourseDTO> iPage) {

        List<CourseVO> courseVOList = courseConverter.dtoListToVOList(iPage.getRecords());

        QueryCourseVO queryCourseVO = new QueryCourseVO();

        queryCourseVO.setCourses(courseVOList);
        queryCourseVO.setTotal(iPage.getTotal());
        return queryCourseVO;
    }
}




