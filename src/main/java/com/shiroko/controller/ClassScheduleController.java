package com.shiroko.controller;

import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.dto.classschedule.QueryClassScheduleDTO;
import com.shiroko.repository.vo.classschedule.QueryClassScheduleVO;
import com.shiroko.service.ClassScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description: 班级排课控制器
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/16 下午10:26
 */
@RestController
@RequestMapping("/class_schedule")
@RequiredArgsConstructor
public class ClassScheduleController {

    private final ClassScheduleService classScheduleService;

    @PostMapping("/get_by_class_id")
    public ResponseDTO<QueryClassScheduleVO> getByClassId(@Valid @RequestBody QueryClassScheduleDTO dto) {
        return classScheduleService.getByClassId(dto);
    }

    @PostMapping("/get_by_institution_id")
    public ResponseDTO<QueryClassScheduleVO> getByInstitutionId(@Valid @RequestBody QueryClassScheduleDTO dto) {
        return classScheduleService.getByInstitutionId(dto);
    }
}
