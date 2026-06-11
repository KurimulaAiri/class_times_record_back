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
public class SysUserVO extends BaseVO {

    private Long id;

    private String username;

    private String nickname;

    private String phone;

    private String email;

    private String avatar;

    private Integer status;

    private String createTimeStr;

    private String updateTimeStr;

    private String remark;

    private List<Long> roleIds;
}
