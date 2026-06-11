package com.shiroko.repository.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@TableName(value = "sys_role_menu")
@Data
public class SysRoleMenu implements Serializable {

    private Long roleId;

    private Long menuId;

    @Serial
    private static final long serialVersionUID = 1L;
}
