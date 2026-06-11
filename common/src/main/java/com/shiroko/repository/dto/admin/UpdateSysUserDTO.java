package com.shiroko.repository.dto.admin;

import com.shiroko.repository.dto.BaseDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateSysUserDTO extends BaseDTO {

    @NotNull(message = "用户ID不能为空")
    private Long id;

    private String nickname;

    private String phone;

    private String email;

    private Integer status;

    private String remark;

    private List<Long> roleIds;
}
