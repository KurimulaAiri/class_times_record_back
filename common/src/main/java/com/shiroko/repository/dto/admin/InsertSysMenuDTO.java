package com.shiroko.repository.dto.admin;

import com.shiroko.repository.dto.BaseDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class InsertSysMenuDTO extends BaseDTO {

    private Long parentId;

    @NotBlank(message = "菜单名称不能为空")
    private String menuName;

    @NotBlank(message = "菜单类型不能为空")
    private String menuType;

    private String path;

    private String component;

    private String perms;

    private String icon;

    private Integer sort;

    private Integer status;
}
