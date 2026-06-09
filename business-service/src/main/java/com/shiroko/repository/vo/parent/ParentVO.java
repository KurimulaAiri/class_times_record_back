package com.shiroko.repository.vo.parent;

import lombok.Data;

/**
 * Description: 家长VO类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/7 上午1:04
 */
@Data
public class ParentVO {
    private String username;
    private Long parentId;
    private Long studentId;  // 必须包含此字段，用于 Service 层匹配
    private String relation;
    private String phone;
    private Integer isPrimary;
}
