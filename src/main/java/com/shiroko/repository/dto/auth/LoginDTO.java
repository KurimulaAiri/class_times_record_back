package com.shiroko.repository.dto.auth;

import com.shiroko.repository.dto.auth.validategroup.LoginGroup;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description: 登录DTO
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/3/19 下午4:10
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginDTO {
    @NotNull(message = "code不能为空", groups = LoginGroup.LoginNoPwd.class)
    private String code;

    @NotNull(message = "openId不能为空", groups = {LoginGroup.LoginByPwd.class, LoginGroup.LoginByToken.class})
    private String openId;

    @NotNull(message = "role不能为空", groups = {LoginGroup.LoginByPwd.class, LoginGroup.LoginNoPwd.class})
    private Long role;

    @NotNull(message = "account不能为空", groups = LoginGroup.LoginByPwd.class)
    private String account;

    @NotNull(message = "password不能为空", groups = LoginGroup.LoginByPwd.class)
    private String password;

    @NotNull(message = "token不能为空", groups = {LoginGroup.LoginByToken.class, LoginGroup.LoginOut.class})
    private String token;
}
