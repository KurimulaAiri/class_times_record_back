package com.shiroko.repository.dto;

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
    private String code;
    private String openId;
    private String role;
    private String account;
    private String password;
}
