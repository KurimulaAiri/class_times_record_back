package com.shiroko.repository.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Description: 绑定权限记录DTO类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/3/24 上午12:56
 */
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BindPermissionRecordDTO extends BaseDTO{
    private Long courseRecordId;
    private String permissionType;
}
