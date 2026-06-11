package com.shiroko.repository.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@TableName(value = "sys_user_role")
@Data
public class SysUserRole implements Serializable {

    private Long userId;

    private Long roleId;

    @Serial
    private static final long serialVersionUID = 1L;
}
