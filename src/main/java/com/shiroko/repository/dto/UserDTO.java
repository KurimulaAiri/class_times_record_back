package com.shiroko.repository.dto;

import com.shiroko.repository.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Description: 用户DTO类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/4/19 上午12:57
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class UserDTO extends User {
    private Long roleId;
}
