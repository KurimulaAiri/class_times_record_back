package com.shiroko.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.dto.institution.QueryInstitutionDTO;
import com.shiroko.repository.dto.institution.UpdateInstitutionDTO;
import com.shiroko.repository.entity.Institution;
import com.shiroko.repository.vo.institution.QueryInstitutionVO;
import com.shiroko.repository.vo.institution.UpdateInstitutionVO;

/**
 * Description: 机构服务类接口
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/4 下午16:43
 */
public interface InstitutionService extends IService<Institution> {

    ResponseDTO<QueryInstitutionVO> getInstitutionByStudentId(QueryInstitutionDTO queryInstitutionDTO);

    ResponseDTO<QueryInstitutionVO> getInstitutionByOpenId(QueryInstitutionDTO queryInstitutionDTO);

    ResponseDTO<QueryInstitutionVO> getInstitutionByCode(QueryInstitutionDTO queryInstitutionDTO);

    ResponseDTO<UpdateInstitutionVO> updateInstitution(UpdateInstitutionDTO updateInstitutionDTO);

    ResponseDTO<QueryInstitutionVO> getInstitutionById(QueryInstitutionDTO queryInstitutionDTO);
}
