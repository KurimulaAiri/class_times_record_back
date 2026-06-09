package com.shiroko.repository.dto.parent;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Description: 插入家长DTO类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/10 下午11:10
 */
@Data
public class InsertParentDTO {

    @NotBlank(message = "家长姓名不能为空")
    private String username;

    @NotBlank(message = "家长关系不能为空")
    private String relation;

    @NotBlank(message = "家长手机号不能为空")
    private String phone;

}
