package com.shiroko.converter;

import com.shiroko.annotation.BaseDateTimeToString;
import com.shiroko.repository.entity.SysMenu;
import com.shiroko.repository.vo.admin.SysMenuVO;
import com.shiroko.util.DateTransformUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {DateTransformUtils.class})
public interface SysMenuConverter extends BaseConverter<SysMenu, SysMenuVO> {

    @Mapping(target = "createTimeStr", source = "createTime", qualifiedBy = BaseDateTimeToString.class)
    @Mapping(target = "updateTimeStr", source = "updateTime", qualifiedBy = BaseDateTimeToString.class)
    @Mapping(target = "children", ignore = true)
    @Override
    SysMenuVO pojoToVO(SysMenu pojo);
}
