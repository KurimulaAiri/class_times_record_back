package com.shiroko.repository.dto.admin;

import com.shiroko.repository.dto.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class QuerySysRoleDTO extends BaseDTO {

    private String roleName;

    private String roleKey;

    private Integer status;

    private Integer currentPage;

    private Integer pageSize;
}
