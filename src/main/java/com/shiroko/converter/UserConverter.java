package com.shiroko.converter;

import com.shiroko.repository.dto.UserDTO;
import com.shiroko.repository.entity.User;
import com.shiroko.repository.vo.UserVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Description: 用户转换器接口
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/3/19 下午11:52
 */
@Mapper(componentModel = "spring")
public interface UserConverter extends BaseConverter<User, UserVO> {
    @Mapping(source = "createTime", target = "createTimeStr", qualifiedByName = "dateToString")
    @Mapping(source = "updateTime", target = "updateTimeStr", qualifiedByName = "dateToString")
    @Override
    UserVO pojoToVO(User pojo);

    @Mapping(target = "roleId", source = "roleId")
    UserDTO pojoToDTO(User pojo, Long roleId);
}
