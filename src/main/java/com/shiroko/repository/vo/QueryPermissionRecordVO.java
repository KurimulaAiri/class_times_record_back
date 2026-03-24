package com.shiroko.repository.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Description: 查询权限记录VO
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/3/24 下午3:01
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Accessors(chain = true)
@AllArgsConstructor
public class QueryPermissionRecordVO extends BaseVO {
    private List<PermissionRecordVO> permissionRecords;
    private Long total;
}
