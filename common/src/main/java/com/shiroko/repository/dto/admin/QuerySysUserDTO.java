package com.shiroko.repository.dto.admin;

import com.shiroko.repository.dto.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class QuerySysUserDTO extends BaseDTO {

    private String username;

    private String phone;

    private Integer status;

    private Integer currentPage;

    private Integer pageSize;
}
