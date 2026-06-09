package com.shiroko.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shiroko.repository.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * Description: 用户Mapper接口
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/3/19 下午11:34
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    User selectUserByPlatformOpenid(String platform, String openId);

    User selectUserByPlatformOpenidAndInstitution(String platform, String openId, Long institutionId);
}




