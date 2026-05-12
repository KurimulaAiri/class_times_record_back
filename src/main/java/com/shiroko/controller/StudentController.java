package com.shiroko.controller;

import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.dto.student.InsertStudentDTO;
import com.shiroko.repository.dto.student.QueryStudentDTO;
import com.shiroko.repository.dto.student.UpdateStudentDTO;
import com.shiroko.repository.dto.student.validategroup.QueryGroup;
import com.shiroko.repository.vo.student.InsertStudentVO;
import com.shiroko.repository.vo.student.QueryStudentVO;
import com.shiroko.repository.vo.student.UpdateStudentVO;
import com.shiroko.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/get_by_student_id")
    public ResponseDTO<QueryStudentVO> getStudentById(@Validated(QueryGroup.ByStudentId.class) @RequestBody QueryStudentDTO queryStudentDTO) {
        return studentService.getStudentByStudentId(queryStudentDTO);
    }
    @PostMapping("/get_by_parent_id")
    public ResponseDTO<QueryStudentVO> getStudentByParentId(@Validated(QueryGroup.ByParentId.class) @RequestBody QueryStudentDTO queryStudentDTO) {
        return studentService.getStudentByParentId(queryStudentDTO);
    }

    @PostMapping("/get_by_teacher_id")
    public ResponseDTO<QueryStudentVO> getStudentByTeacherId(@Validated(QueryGroup.ByTeacherId.class) @RequestBody QueryStudentDTO queryStudentDTO) {
        return studentService.getStudentByTeacherId(queryStudentDTO);
    }

    @PostMapping("/get_by_class_id")
    public ResponseDTO<QueryStudentVO> getStudentByClassId(@Validated(QueryGroup.ByClassId.class) @RequestBody QueryStudentDTO queryStudentDTO) {
        return studentService.getStudentByClassId(queryStudentDTO);
    }

    @PostMapping("/get_by_institution_id")
    public ResponseDTO<QueryStudentVO> getStudentByInstitutionId(@Validated(QueryGroup.ByInstitutionId.class) @RequestBody QueryStudentDTO queryStudentDTO) {
        return studentService.getStudentByInstitutionId(queryStudentDTO);
    }

    @PostMapping("/insert")
    public ResponseDTO<InsertStudentVO> insert(@RequestBody InsertStudentDTO insertStudentDTO) {
        return studentService.insertStudent(insertStudentDTO);
    }

    @PostMapping("/update")
    public ResponseDTO<UpdateStudentVO> update(@Valid @RequestBody UpdateStudentDTO updateStudentDTO) {
        return studentService.updateStudent(updateStudentDTO);
    }

}
