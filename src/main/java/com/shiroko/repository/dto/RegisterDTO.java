package com.shiroko.repository.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description: 注册请求参数
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/4/16 上午12:22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDTO {
    @NotBlank(message = "账号不能为空")
    private String account;    // 账号
    @NotBlank(message = "密码不能为空")
    private String password;   // 密码 （SM2 加密后的密文）
    @NotBlank(message = "角色不能为空")
    private String role;       // 角色 （teacher / parent）
    @NotBlank(message = "openid不能为空")
    private String openId;     // 微信openid
}
