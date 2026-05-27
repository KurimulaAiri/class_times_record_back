package com.shiroko.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiroko.mapper.InstitutionMapper;
import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.dto.institution.QueryInstitutionDTO;
import com.shiroko.repository.entity.Institution;
import com.shiroko.repository.vo.institution.QueryInstitutionVO;
import com.shiroko.service.InstitutionService;
import lombok.RequiredArgsConstructor;
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

    @Override
    public ResponseDTO<QueryInstitutionVO> getInstitutionByOpenId(QueryInstitutionDTO queryInstitutionDTO) {

        List<Institution> institutions = institutionMapper.selectListByOpenId(queryInstitutionDTO.getPlatform(), queryInstitutionDTO.getOpenId());

        return ResponseDTO.success(new QueryInstitutionVO(institutions));
    }

    @Override
    public ResponseDTO<QueryInstitutionVO> getInstitutionByCode(QueryInstitutionDTO queryInstitutionDTO) {
        Institution institution = institutionMapper.selectOne(new LambdaQueryWrapper<Institution>()
                .eq(Institution::getInstitutionCode, queryInstitutionDTO.getInstitutionCode()));
        if (institution == null) {
            return ResponseDTO.fail("机构不存在");
        }
        return ResponseDTO.success(new QueryInstitutionVO(List.of(institution)));
    }
}




