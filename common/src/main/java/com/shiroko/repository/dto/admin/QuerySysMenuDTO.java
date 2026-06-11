package com.shiroko.repository.dto.admin;

import com.shiroko.repository.dto.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class QuerySysMenuDTO extends BaseDTO {

    private String menuName;

    private String menuType;

    private Integer status;
}
