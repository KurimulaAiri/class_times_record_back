package com.shiroko.repository.dto;

import com.shiroko.repository.entity.User;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Description: 用户DTO类
 * <p>
 * 目前用作用户登录时的参数传递，多加入一个权限 id 字段，存入 UserContext 中
 * @author Guguguy
 * @version 1.0
 * @since 2026/4/19 上午12:57
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO extends User {
    private Long roleId;
}
