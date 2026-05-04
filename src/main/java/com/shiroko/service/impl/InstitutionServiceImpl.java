package com.shiroko.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.dto.institution.QueryInstitutionDTO;
import com.shiroko.repository.entity.Institution;
import com.shiroko.repository.vo.institution.QueryInstitutionVO;
import com.shiroko.service.InstitutionService;
import com.shiroko.mapper.InstitutionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description: 机构服务接口实现
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/4 下午16:43
 */
@Service
@RequiredArgsConstructor
public class InstitutionServiceImpl extends ServiceImpl<InstitutionMapper, Institution> implements InstitutionService{

    private final InstitutionMapper institutionMapper;

    @Override
    public ResponseDTO<QueryInstitutionVO> getInstitutionByStudentId(QueryInstitutionDTO queryInstitutionDTO) {
        List<Institution> institutions = institutionMapper.selectListByStudentId(queryInstitutionDTO.getStudentId());
        return ResponseDTO.success(new QueryInstitutionVO(institutions));
    }
}




