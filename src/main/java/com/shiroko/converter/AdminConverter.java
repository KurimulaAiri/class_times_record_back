package com.shiroko.converter;

import com.shiroko.annotation.BaseDateTimeToString;
import com.shiroko.repository.entity.Admin;
import com.shiroko.repository.vo.admin.AdminVO;
import com.shiroko.util.DateTransformUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Description: 管理员转换器
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/6/5 上午2:33
 */
@Mapper(componentModel = "spring", uses = {DateTransformUtils.class})
public interface AdminConverter extends BaseConverter<Admin, AdminVO> {

    @Mapping(target = "createTimeStr", source = "createTime", qualifiedBy = BaseDateTimeToString.class)
    @Mapping(target = "updateTimeStr", source = "updateTime", qualifiedBy = BaseDateTimeToString.class)
    @Override
    AdminVO pojoToVO(Admin admin);
}
