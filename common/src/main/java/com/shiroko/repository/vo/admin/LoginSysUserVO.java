package com.shiroko.repository.vo.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginSysUserVO {

    private String accessToken;

    private String refreshToken;

    private SysUserVO userInfo;
}
