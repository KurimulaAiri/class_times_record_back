package com.shiroko.converter;

import com.shiroko.annotation.BaseDateTimeToString;
import com.shiroko.repository.dto.UserDTO;
import com.shiroko.repository.entity.User;
import com.shiroko.repository.entity.common.RoleBaseEntity;
import com.shiroko.repository.vo.UserVO;
import com.shiroko.util.DateTransformUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Description: 用户转换器接口
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/3/19 下午11:52
 */
@Mapper(componentModel = "spring", uses = {DateTransformUtils.class})
public interface UserConverter extends BaseConverter<User, UserVO<RoleBaseEntity>> {

    @Mapping(target = "userId", source = "id")
    @Mapping(source = "createTime", target = "createTimeStr", qualifiedBy = BaseDateTimeToString.class)
    @Mapping(source = "updateTime", target = "updateTimeStr", qualifiedBy = BaseDateTimeToString.class)
    @Override
    UserVO<RoleBaseEntity> pojoToVO(User pojo);

    @Mapping(target = "roleId", source = "roleId")
    UserDTO pojoToDTO(User pojo, Long roleId);
}
