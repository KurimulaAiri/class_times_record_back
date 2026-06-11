package com.shiroko.repository.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Description: 用户DTO类
 * <p>
 * 目前用作用户登录时的参数传递，多加入一个权限 id 字段，存入 UserContext 中
 * @author Guguguy
 * @version 1.0
 * @since 2026/4/19 上午12:57
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO  {
    private Long roleId;
    private Long id;
    private Long institutionId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
