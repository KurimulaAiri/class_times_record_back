package com.shiroko.converter;

import com.shiroko.repository.entity.Admin;
import com.shiroko.repository.vo.admin.AdminVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Description: 管理员转换器
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/6/5 上午2:33
 */
@Mapper(componentModel = "spring")
public interface AdminConverter extends BaseConverter<Admin, AdminVO> {

    @Mapping(target = "createTimeStr", source = "createTime", qualifiedByName = "dateTimeToString")
    @Mapping(target = "updateTimeStr", source = "updateTime", qualifiedByName = "dateTimeToString")
    @Override
    AdminVO pojoToVO(Admin admin);
}
