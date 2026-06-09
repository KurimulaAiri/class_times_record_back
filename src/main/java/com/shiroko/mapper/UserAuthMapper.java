package com.shiroko.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shiroko.repository.entity.UserAuth;
import org.apache.ibatis.annotations.Param;

/**
 * Description: 閺夊啴妾虹€圭偘缍嬬猾?
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/4/17 娑撳宕?2:40
 */
public interface UserAuthMapper extends BaseMapper<UserAuth> {

    UserAuth selectAuthByAccountAndInstitution(String account, Long roleId, Long institutionId);

    UserAuth selectAuthByTeacherId(Long teacherId);

    // UserAuthMapper.java
    boolean existsByInstitutionAndAccountAndRole(
            @Param("institutionId") Long institutionId,
            @Param("account") String account,
            @Param("roleId") Long roleId
    );
}



