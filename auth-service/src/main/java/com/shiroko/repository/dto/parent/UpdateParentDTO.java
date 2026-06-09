package com.shiroko.repository.dto.parent;

import lombok.Data;

/**
 * Description: 家长DTO类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/12 下午11:37
 */
@Data
public class UpdateParentDTO {
    private String username;
    private Long parentId;
    private Long studentId;
    private String relation;
    private Boolean isPrimary;
    private String phone;
}
