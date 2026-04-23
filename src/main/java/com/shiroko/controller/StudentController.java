package com.shiroko.controller;

import com.shiroko.repository.dto.QueryStudentDTO;
import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.vo.QueryStudentVO;
import com.shiroko.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * Description: 学生控制器
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/4/24 上午1:04
 */
@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping("/get_by_parent_id")
    public ResponseDTO<QueryStudentVO> getStudentByParentId(@RequestBody QueryStudentDTO queryStudentDTO) {
        return studentService.getStudentByParentId(queryStudentDTO);
    }

}
