package com.shiroko.repository.dto.parent;

import lombok.Data;

/**
 * Description: 家长数据库实体类的DTO类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/15 上午1:42
 */
@Data
public class ParentDTO {
    private String username;
    private Long parentId;
    private Long studentId;  // 必须包含此字段，用于 Service 层匹配
    private String relation;
    private String phone;
    private Integer isPrimary;
}
