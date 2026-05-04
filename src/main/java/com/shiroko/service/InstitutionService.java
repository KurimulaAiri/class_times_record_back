package com.shiroko.service;

import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.dto.institution.QueryInstitutionDTO;
import com.shiroko.repository.entity.Institution;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shiroko.repository.vo.institution.QueryInstitutionVO;

/**
 * Description: 机构服务类接口
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/4 下午16:43
 */
public interface InstitutionService extends IService<Institution> {

    ResponseDTO<QueryInstitutionVO> getInstitutionByStudentId(QueryInstitutionDTO queryInstitutionDTO);
}
