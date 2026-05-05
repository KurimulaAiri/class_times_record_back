package com.shiroko.controller;

import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.dto.institution.QueryInstitutionDTO;
import com.shiroko.repository.vo.institution.QueryInstitutionVO;
import com.shiroko.service.InstitutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description: 机构控制器
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/4 下午4:46
 */
@RestController
@RequestMapping("/institution")
@RequiredArgsConstructor
public class InstitutionController {

    private final InstitutionService institutionService;

    @PostMapping("/get_institution_by_student_id")
    public ResponseDTO<QueryInstitutionVO> getInstitutionByStudentId(@RequestBody QueryInstitutionDTO queryInstitutionDTO) {
        return institutionService.getInstitutionByStudentId(queryInstitutionDTO);
    }

}
