package com.shiroko.repository.vo.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description: 获取权限信息VO
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/6/8 下午9:26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetUserAuthInfoVO {
    private String account;
}
