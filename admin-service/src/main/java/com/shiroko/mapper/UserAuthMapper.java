package com.shiroko.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shiroko.repository.entity.UserAuth;
import org.apache.ibatis.annotations.Param;

public interface UserAuthMapper extends BaseMapper<UserAuth> {
    UserAuth selectAuthByTeacherId(@Param("teacherId") Long teacherId);
}
