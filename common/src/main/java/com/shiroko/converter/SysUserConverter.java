package com.shiroko.converter;

import com.shiroko.annotation.BaseDateTimeToString;
import com.shiroko.repository.entity.SysUser;
import com.shiroko.repository.vo.admin.SysUserVO;
import com.shiroko.util.DateTransformUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {DateTransformUtils.class})
public interface SysUserConverter extends BaseConverter<SysUser, SysUserVO> {

    @Mapping(target = "createTimeStr", source = "createTime", qualifiedBy = BaseDateTimeToString.class)
    @Mapping(target = "updateTimeStr", source = "updateTime", qualifiedBy = BaseDateTimeToString.class)
    @Mapping(target = "roleIds", ignore = true)
    @Override
    SysUserVO pojoToVO(SysUser pojo);
}
