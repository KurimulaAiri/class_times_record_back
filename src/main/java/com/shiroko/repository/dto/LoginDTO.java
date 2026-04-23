package com.shiroko.repository.dto;

import com.shiroko.repository.dto.group.LoginGroup;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description: TODO
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

    @NotNull(message = "openId不能为空", groups = LoginGroup.LoginByPwd.class)
    private String openId;

    @NotNull(message = "role不能为空", groups = LoginGroup.LoginByPwd.class)
    private Long role;

    @NotNull(message = "account不能为空", groups = LoginGroup.LoginByPwd.class)
    private String account;

    @NotNull(message = "password不能为空", groups = LoginGroup.LoginByPwd.class)
    private String password;
}
