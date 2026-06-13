package com.shiroko.controller;

import com.shiroko.annotation.OperationLog;
import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.dto.admin.InsertSysUserDTO;
import com.shiroko.repository.dto.admin.LoginSysUserDTO;
import com.shiroko.repository.dto.admin.QuerySysUserDTO;
import com.shiroko.repository.dto.admin.UpdateSysUserDTO;
import com.shiroko.repository.entity.SysRole;
import com.shiroko.repository.vo.admin.LoginSysUserVO;
import com.shiroko.repository.vo.admin.SysUserVO;
import com.shiroko.service.SysUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class SysUserController {

    private final SysUserService sysUserService;

    @PostMapping("/login")
    @OperationLog("管理员登录")
    public ResponseDTO<LoginSysUserVO> login(@Valid @RequestBody LoginSysUserDTO dto) {
        return sysUserService.login(dto);
    }

    @PostMapping("/list")
    public ResponseDTO<Map<String, Object>> listUsers(@RequestBody QuerySysUserDTO dto) {
        return sysUserService.listUsers(dto);
    }

    @PostMapping("/get_by_id")
    public ResponseDTO<SysUserVO> getUserById(@RequestBody Map<String, Long> params) {
        return sysUserService.getUserById(params.get("id"));
    }

    @PostMapping("/insert")
    public ResponseDTO<SysUserVO> insertUser(@Valid @RequestBody InsertSysUserDTO dto) {
        return sysUserService.insertUser(dto);
    }

    @PostMapping("/update")
    public ResponseDTO<SysUserVO> updateUser(@Valid @RequestBody UpdateSysUserDTO dto) {
        return sysUserService.updateUser(dto);
    }

    @PostMapping("/delete")
    public ResponseDTO<String> deleteUser(@RequestBody Map<String, Long> params) {
        return sysUserService.deleteUser(params.get("id"));
    }

    @PostMapping("/reset_password")
    @OperationLog("重置管理员密码")
    public ResponseDTO<String> resetPassword(@RequestBody Map<String, Object> params) {
        Long id = Long.parseLong(params.get("id").toString());
        String newPassword = params.get("newPassword").toString();
        return sysUserService.resetPassword(id, newPassword);
    }

    @PostMapping("/get_roles")
    public ResponseDTO<List<SysRole>> getUserRoles(@RequestBody Map<String, Long> params) {
        return sysUserService.getUserRoles(params.get("userId"));
    }

    @PostMapping("/refresh")
    public ResponseDTO<LoginSysUserVO> refreshToken(@RequestBody Map<String, String> params) {
        return sysUserService.refreshToken(params.get("refreshToken"));
    }
}
