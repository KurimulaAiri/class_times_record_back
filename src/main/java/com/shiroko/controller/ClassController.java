package com.shiroko.controller;

import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.dto.clazz.InsertClassDTO;
import com.shiroko.repository.dto.clazz.QueryClassDTO;
import com.shiroko.repository.dto.clazz.UpdateClassDTO;
import com.shiroko.repository.vo.clazz.InsertClassVO;
import com.shiroko.repository.vo.clazz.QueryClassVO;
import com.shiroko.repository.vo.clazz.UpdateClassVO;
import com.shiroko.service.ClassService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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

    @PostMapping("/get_class_by_id")
    public ResponseDTO<QueryClassVO> getClassById(@RequestBody QueryClassDTO queryClassDTO) {
        return classService.getClassById(queryClassDTO);
    }

    @PostMapping("/add_student_to_class")
    public ResponseDTO<UpdateClassVO> addStudentToClass(@RequestBody UpdateClassDTO updateClassDTO) {
        return classService.addStudentToClass(updateClassDTO);
    }

    @PostMapping("/insert")
    public ResponseDTO<InsertClassVO> insertClass(@RequestBody InsertClassDTO insertClassDTO) {
        return classService.insertClass(insertClassDTO);
    }
}
