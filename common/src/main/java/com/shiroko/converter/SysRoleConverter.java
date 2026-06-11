package com.shiroko.converter;

import com.shiroko.annotation.BaseDateTimeToString;
import com.shiroko.repository.entity.SysRole;
import com.shiroko.repository.vo.admin.SysRoleVO;
import com.shiroko.util.DateTransformUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {DateTransformUtils.class})
public interface SysRoleConverter extends BaseConverter<SysRole, SysRoleVO> {

    @Mapping(target = "createTimeStr", source = "createTime", qualifiedBy = BaseDateTimeToString.class)
    @Mapping(target = "updateTimeStr", source = "updateTime", qualifiedBy = BaseDateTimeToString.class)
    @Mapping(target = "menuIds", ignore = true)
    @Override
    SysRoleVO pojoToVO(SysRole pojo);
}
