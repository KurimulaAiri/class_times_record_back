package com.shiroko.repository.vo.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description: 管理员VO类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/6/5 上午2:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminVO {
    /**
     * 管理员id
     */
    private Long adminId;
    /**
     * 创建时间
     */
    private String createTimeStr;
    /**
     * 更新时间
     */
    private String updateTimeStr;

    /**
     * 对于user_id
     */
    private Long userId;

    /**
     * 是否可用
     */
    private Boolean isAvailable;

    /**
     * 用户名
     */
    private String username;
}
