package com.shiroko.repository.vo;

import com.shiroko.repository.entity.RoleBaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description: TODO
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/3/19 下午4:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginVO {
    private String token;
    private String openId;
    private UserVO<RoleBaseEntity> user;
}
