package com.shiroko.controller;

import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.dto.courserecord.*;
import com.shiroko.repository.dto.courserecord.validategroup.InsertGroup;
import com.shiroko.repository.vo.courserecord.DeductCourseRecordVO;
import com.shiroko.repository.vo.courserecord.QueryCourseRecordVO;
import com.shiroko.service.CourseRecordService;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description: 课程记录控制器
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
        // System.out.println("dto = " + dto);
        return courseRecordService.getCourseRecords(dto);
    }

    @PostMapping("/new_get")
    public ResponseDTO<QueryCourseRecordVO> newGetCourseRecords(@Valid @RequestBody QueryCourseRecordDTO dto) {
        return courseRecordService.newGetCourseRecords(dto);
    }

    @PostMapping("/add")
    public ResponseDTO<Object> addCourseRecord(@Validated(InsertGroup.OldGroup.class) @RequestBody InsertCourseRecordDTO insertCourseRecordDTO) {
        return courseRecordService.addCourseRecord(insertCourseRecordDTO);
    }

    @PostMapping("/insert")
    public ResponseDTO<Object> insertCourseRecord(@Validated(InsertGroup.NewGroup.class) @RequestBody InsertCourseRecordDTO insertCourseRecordDTO) {
        return courseRecordService.insertCourseRecord(insertCourseRecordDTO);
    }

    @PostMapping("/delete")
    public ResponseDTO<Object> deleteCourseRecord(@Valid @RequestBody DeleteCourseRecordDTO deleteCourseRecordDTO) {
        return courseRecordService.deleteCourseRecord(deleteCourseRecordDTO);
    }

    @PostMapping("/deduct_by_student_id")
    public ResponseDTO<DeductCourseRecordVO> deductByStudentId(@Validated @RequestBody DeductCourseRecordDTO deductCourseRecordDTO) {
        return courseRecordService.deductByStudentId(deductCourseRecordDTO);
    }

    @PostMapping("/deduct_by_course_id")
    public ResponseDTO<DeductCourseRecordVO> deductByCourseId(@Validated @RequestBody DeductCourseRecordDTO deductCourseRecordDTO) {
        return courseRecordService.deductByCourseId(deductCourseRecordDTO);
    }
    @PostMapping("/update")
    public ResponseDTO<Object> updateCourseRecord(@Valid @RequestBody UpdateCourseRecordDTO updateCourseRecordDTO) {
        // System.out.println(updateCourseRecordDTO);
        return courseRecordService.updateCourseRecord(updateCourseRecordDTO);
    }
}
