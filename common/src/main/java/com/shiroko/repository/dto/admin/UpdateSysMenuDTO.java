package com.shiroko.repository.dto.admin;

import com.shiroko.repository.dto.BaseDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateSysMenuDTO extends BaseDTO {

    @NotNull(message = "菜单ID不能为空")
    private Long id;

    private Long parentId;

    private String menuName;

    private String menuType;

    private String path;

    private String component;

    private String perms;

    private String icon;

    private Integer sort;

    private Integer status;
}
