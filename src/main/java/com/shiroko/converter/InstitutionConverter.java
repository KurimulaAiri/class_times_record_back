package com.shiroko.converter;

import com.shiroko.repository.dto.institution.UpdateInstitutionDTO;
import com.shiroko.repository.entity.Institution;
import com.shiroko.repository.vo.institution.InstitutionVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Description: 机构转换器
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/31 下午3:08
 */
@Mapper(componentModel = "spring")
public interface InstitutionConverter extends BaseConverter<Institution, InstitutionVO> {

    @Mapping(target = "id", source = "institutionId")
    Institution updateDtoToPojo(UpdateInstitutionDTO updateInstitutionDTO);

    @Mapping(target = "updateTimeStr", source = "updateTime", qualifiedByName = "dateTimeToString")
    @Mapping(target = "createTimeStr", source = "createTime", qualifiedByName = "dateTimeToString")
    @Override
    InstitutionVO pojoToVO(Institution institution);

}
