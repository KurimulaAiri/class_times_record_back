package com.shiroko.repository.vo.admin;

import com.shiroko.repository.vo.BaseVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysRoleVO extends BaseVO {

    private Long id;

    private String roleName;

    private String roleKey;

    private Integer sort;

    private Integer status;

    private String createTimeStr;

    private String updateTimeStr;

    private String remark;

    private List<Long> menuIds;
}
