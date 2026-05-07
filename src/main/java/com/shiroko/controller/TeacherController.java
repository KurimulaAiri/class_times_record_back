package com.shiroko.controller;

import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.dto.teacher.QueryTeacherDTO;
import com.shiroko.repository.vo.teacher.QueryTeacherVO;
import com.shiroko.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description: 教师控制器
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/7 下午6:28
 */
@RestController
@RequestMapping("/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    @PostMapping("/get_by_id")
    public ResponseDTO<QueryTeacherVO> getTeacherById(@RequestBody QueryTeacherDTO queryTeacherDTO) {
        return teacherService.getTeacherById(queryTeacherDTO);
    }
}
