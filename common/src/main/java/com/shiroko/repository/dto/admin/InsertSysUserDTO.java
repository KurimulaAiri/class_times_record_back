package com.shiroko.repository.dto.admin;

import com.shiroko.repository.dto.BaseDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class InsertSysUserDTO extends BaseDTO {

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "昵称不能为空")
    private String nickname;

    @NotBlank(message = "密码不能为空")
    private String password;

    private String phone;

    private String email;

    private Integer status;

    private String remark;

    private List<Long> roleIds;
}
