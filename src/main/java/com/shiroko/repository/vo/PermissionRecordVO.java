package com.shiroko.repository.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description: 权限记录VO
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/3/24 下午2:49
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermissionRecordVO {
    /**
     * 表id
     */
    private Long id;

    private Long permissionType;
}
