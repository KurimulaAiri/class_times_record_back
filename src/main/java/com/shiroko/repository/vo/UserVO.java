package com.shiroko.repository.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description: 用户VO类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/3/19 下午11:21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVO {
    private Long userId;
    private String openid;
    private String account;
    private String createTimeStr;
    private String updateTimeStr;
   }
