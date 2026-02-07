package com.shiroko.controller;

import com.shiroko.repository.dto.QueryCourseRecordDTO;
import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.vo.QueryCourseRecordVO;
import com.shiroko.service.CourseRecordService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Description: TODO
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/2/7 上午12:48
 */
@RestController
@RequestMapping("/course_record")
public class CourseRecordController {

    private final CourseRecordService courseRecordService;

    public CourseRecordController(CourseRecordService courseRecordService) {
        this.courseRecordService = courseRecordService;
    }

    /**
     * 获取课程记录
     *
     * @param dto 查询课程记录DTO
     * @return 课程记录列表
     */
    @PostMapping("/get")
    public ResponseDTO<QueryCourseRecordVO> getCourseRecords(@Valid @RequestBody QueryCourseRecordDTO dto) {
        System.out.println("dto = " + dto);
        return courseRecordService.getCourseRecords(dto);
    }

}
