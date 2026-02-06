package com.shiroko.controller;

import com.alibaba.fastjson2.JSON;
import com.shiroko.repository.dto.QueryCourseRecordDTO;
import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.entity.CourseRecord;
import com.shiroko.repository.vo.QueryCourseRecordVO;
import com.shiroko.service.CourseRecordService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * @param dto 查询课程记录DTO
     * @return 课程记录列表
     */
    @GetMapping("/get")
    public ResponseDTO<List<QueryCourseRecordVO>> getCourseRecords(QueryCourseRecordDTO dto) {
        return courseRecordService.getCourseRecords(dto);

}
