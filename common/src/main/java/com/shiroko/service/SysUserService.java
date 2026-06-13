package com.shiroko.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.dto.admin.InsertSysUserDTO;
import com.shiroko.repository.dto.admin.LoginSysUserDTO;
import com.shiroko.repository.dto.admin.QuerySysUserDTO;
import com.shiroko.repository.dto.admin.UpdateSysUserDTO;
import com.shiroko.repository.entity.SysRole;
import com.shiroko.repository.entity.SysUser;
import com.shiroko.repository.vo.admin.LoginSysUserVO;
import com.shiroko.repository.vo.admin.SysUserVO;

import java.util.List;
import java.util.Map;

public interface SysUserService extends IService<SysUser> {

    ResponseDTO<LoginSysUserVO> login(LoginSysUserDTO dto);

    ResponseDTO<Map<String, Object>> listUsers(QuerySysUserDTO dto);

    ResponseDTO<SysUserVO> getUserById(Long id);

    ResponseDTO<SysUserVO> insertUser(InsertSysUserDTO dto);

    ResponseDTO<SysUserVO> updateUser(UpdateSysUserDTO dto);

    ResponseDTO<String> deleteUser(Long id);

    ResponseDTO<String> resetPassword(Long id, String newPassword);

    ResponseDTO<List<SysRole>> getUserRoles(Long userId);

    ResponseDTO<LoginSysUserVO> refreshToken(String refreshToken);
}
