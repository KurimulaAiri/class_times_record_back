package com.shiroko.repository.vo;

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
    private String openid;
}
