package com.shiroko.controller;

import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.dto.clazz.QueryClassDTO;
import com.shiroko.repository.vo.clazz.QueryClassVO;
import com.shiroko.service.ClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description: 班级控制器
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/7 下午9:01
 */
@RestController
@RequestMapping("/class")
@RequiredArgsConstructor
public class ClassController {

    private final ClassService classService;

    @PostMapping("/get_classes_by_student_id")
    public ResponseDTO<QueryClassVO> getClassesByStudentId(@RequestBody QueryClassDTO queryClassDTO) {
        return classService.getClassesByStudentId(queryClassDTO);
    }

    @PostMapping("/get_classes_by_teacher_id")
    public ResponseDTO<QueryClassVO> getClassesByTeacherId(@RequestBody QueryClassDTO queryClassDTO) {
        return classService.getClassesByTeacherId(queryClassDTO);
    }
}
