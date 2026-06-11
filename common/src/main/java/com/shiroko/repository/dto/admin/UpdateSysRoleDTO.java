package com.shiroko.repository.dto.admin;

import com.shiroko.repository.dto.BaseDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateSysRoleDTO extends BaseDTO {

    @NotNull(message = "角色ID不能为空")
    private Long id;

    private String roleName;

    private String roleKey;

    private Integer sort;

    private Integer status;

    private String remark;

    private List<Long> menuIds;
}
