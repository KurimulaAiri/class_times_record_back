package com.shiroko.controller;

import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.dto.institution.QueryInstitutionDTO;
import com.shiroko.repository.dto.institution.UpdateInstitutionDTO;
import com.shiroko.repository.dto.institution.validategroup.QueryGroup;
import com.shiroko.repository.vo.institution.QueryInstitutionVO;
import com.shiroko.repository.vo.institution.UpdateInstitutionVO;
import com.shiroko.service.InstitutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
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

    @PostMapping("/get_by_id")
    public ResponseDTO<QueryInstitutionVO> getInstitutionById(@RequestBody @Validated(QueryGroup.ById.class) QueryInstitutionDTO queryInstitutionDTO) {
        return institutionService.getInstitutionById(queryInstitutionDTO);
    }

    @PostMapping("/get_institution_by_student_id")
    public ResponseDTO<QueryInstitutionVO> getInstitutionByStudentId(@RequestBody QueryInstitutionDTO queryInstitutionDTO) {
        return institutionService.getInstitutionByStudentId(queryInstitutionDTO);
    }

    @PostMapping("/get_by_open_id")
    public ResponseDTO<QueryInstitutionVO> getInstitutionByOpenId(@RequestBody @Validated(QueryGroup.ByOpenId.class) QueryInstitutionDTO queryInstitutionDTO) {
        return institutionService.getInstitutionByOpenId(queryInstitutionDTO);
    }

    @PostMapping("/get_by_institution_code")
    public ResponseDTO<QueryInstitutionVO> getInstitutionByCode(@RequestBody @Validated(QueryGroup.ByInstitutionCode.class) QueryInstitutionDTO queryInstitutionDTO) {
        return institutionService.getInstitutionByCode(queryInstitutionDTO);
    }

    @PostMapping("/update")
    public ResponseDTO<UpdateInstitutionVO> updateInstitution(@RequestBody @Validated UpdateInstitutionDTO updateInstitutionDTO) {
        return institutionService.updateInstitution(updateInstitutionDTO);
    }

}
