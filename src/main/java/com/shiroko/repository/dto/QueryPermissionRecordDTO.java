package com.shiroko.repository.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description: 查询权限记录DTO
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/3/24 下午2:48
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class QueryPermissionRecordDTO {
    private Long id;
    private Long permissionType;
    private Long userId;
    private Long courseRecordId;
}
