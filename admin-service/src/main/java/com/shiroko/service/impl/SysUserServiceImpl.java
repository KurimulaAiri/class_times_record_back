package com.shiroko.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiroko.converter.SysUserConverter;
import com.shiroko.mapper.SysRoleMapper;
import com.shiroko.mapper.SysUserMapper;
import com.shiroko.mapper.SysUserRoleMapper;
import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.dto.admin.InsertSysUserDTO;
import com.shiroko.repository.dto.admin.LoginSysUserDTO;
import com.shiroko.repository.dto.admin.QuerySysUserDTO;
import com.shiroko.repository.dto.admin.UpdateSysUserDTO;
import com.shiroko.repository.entity.SysRole;
import com.shiroko.repository.entity.SysUser;
import com.shiroko.repository.entity.SysUserRole;
import com.shiroko.repository.vo.admin.LoginSysUserVO;
import com.shiroko.repository.vo.admin.SysUserVO;
import com.shiroko.service.SysUserService;
import com.shiroko.util.JwtUtils;
import com.shiroko.util.SM2Util;
import com.shiroko.util.SM3Util;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    private final SysUserMapper sysUserMapper;
    private final SysUserRoleMapper sysUserRoleMapper;
    private final SysRoleMapper sysRoleMapper;
    private final SysUserConverter sysUserConverter;
    private final JwtUtils jwtUtils;

    @Value("${crypto.sm2.private-key}")
    private String sm2PrivateKey;

    @Override
    public ResponseDTO<LoginSysUserVO> login(LoginSysUserDTO dto) {
        SysUser sysUser = sysUserMapper.selectOne(
                new LambdaQueryWrapper<SysUser>()
                        .eq(SysUser::getUsername, dto.getUsername())
        );
        if (sysUser == null) {
            return ResponseDTO.fail("用户名或密码错误");
        }
        if (sysUser.getStatus() != 1) {
            return ResponseDTO.fail("账号已被禁用");
        }
        // SM2 解密前端传来的密码
        String plainPassword = SM2Util.decrypt(dto.getPassword(), sm2PrivateKey);
        // SM3 带盐值验证密码
        String hashedPassword = SM3Util.digestWithSalt(plainPassword, sysUser.getSalt());
        if (!hashedPassword.equalsIgnoreCase(sysUser.getPassword())) {
            return ResponseDTO.fail("用户名或密码错误");
        }

        String accessToken = jwtUtils.createAccessToken(sysUser.getId(), 0L);
        String refreshToken = jwtUtils.createRefreshToken(sysUser.getId(), 0L);

        SysUserVO userVO = sysUserConverter.pojoToVO(sysUser);
        userVO.setRoleIds(getRoleIdsByUserId(sysUser.getId()));

        return ResponseDTO.success(new LoginSysUserVO(accessToken, refreshToken, userVO));
    }

    @Override
    public ResponseDTO<Map<String, Object>> listUsers(QuerySysUserDTO dto) {
        Page<SysUser> page = new Page<>(dto.getCurrentPage(), dto.getPageSize());

        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        if (dto.getUsername() != null && !dto.getUsername().isEmpty()) {
            wrapper.like(SysUser::getUsername, dto.getUsername());
        }
        if (dto.getPhone() != null && !dto.getPhone().isEmpty()) {
            wrapper.like(SysUser::getPhone, dto.getPhone());
        }
        if (dto.getStatus() != null) {
            wrapper.eq(SysUser::getStatus, dto.getStatus());
        }
        wrapper.orderByDesc(SysUser::getCreateTime);

        Page<SysUser> result = sysUserMapper.selectPage(page, wrapper);

        List<SysUserVO> voList = result.getRecords().stream().map(user -> {
            SysUserVO vo = sysUserConverter.pojoToVO(user);
            vo.setRoleIds(getRoleIdsByUserId(user.getId()));
            return vo;
        }).collect(Collectors.toList());

        Map<String, Object> data = new HashMap<>();
        data.put("list", voList);
        data.put("total", result.getTotal());
        data.put("currentPage", result.getCurrent());
        data.put("pageSize", result.getSize());

        return ResponseDTO.success(data);
    }

    @Override
    public ResponseDTO<SysUserVO> getUserById(Long id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        if (sysUser == null) {
            return ResponseDTO.fail("用户不存在");
        }
        SysUserVO vo = sysUserConverter.pojoToVO(sysUser);
        vo.setRoleIds(getRoleIdsByUserId(id));
        return ResponseDTO.success(vo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<SysUserVO> insertUser(InsertSysUserDTO dto) {
        // Check username uniqueness
        Long count = sysUserMapper.selectCount(
                new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, dto.getUsername())
        );
        if (count > 0) {
            return ResponseDTO.fail("用户名已存在");
        }

        SysUser sysUser = new SysUser();
        sysUser.setUsername(dto.getUsername());
        sysUser.setNickname(dto.getNickname());
        // SM2 解密前端传来的密码，SM3 带盐值哈希存储
        String plainPassword = SM2Util.decrypt(dto.getPassword(), sm2PrivateKey);
        String salt = UUID.randomUUID().toString().replace("-", "");
        sysUser.setPassword(SM3Util.digestWithSalt(plainPassword, salt));
        sysUser.setSalt(salt);
        sysUser.setPhone(dto.getPhone());
        sysUser.setEmail(dto.getEmail());
        sysUser.setStatus(dto.getStatus() != null ? dto.getStatus() : 1);
        sysUser.setRemark(dto.getRemark());

        sysUserMapper.insert(sysUser);

        // Assign roles
        if (dto.getRoleIds() != null && !dto.getRoleIds().isEmpty()) {
            saveUserRoles(sysUser.getId(), dto.getRoleIds());
        }

        SysUserVO vo = sysUserConverter.pojoToVO(sysUser);
        vo.setRoleIds(dto.getRoleIds());
        return ResponseDTO.success(vo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<SysUserVO> updateUser(UpdateSysUserDTO dto) {
        SysUser sysUser = sysUserMapper.selectById(dto.getId());
        if (sysUser == null) {
            return ResponseDTO.fail("用户不存在");
        }

        if (dto.getNickname() != null) {
            sysUser.setNickname(dto.getNickname());
        }
        if (dto.getPhone() != null) {
            sysUser.setPhone(dto.getPhone());
        }
        if (dto.getEmail() != null) {
            sysUser.setEmail(dto.getEmail());
        }
        if (dto.getStatus() != null) {
            sysUser.setStatus(dto.getStatus());
        }
        if (dto.getRemark() != null) {
            sysUser.setRemark(dto.getRemark());
        }

        sysUserMapper.updateById(sysUser);

        // Reassign roles
        if (dto.getRoleIds() != null) {
            sysUserRoleMapper.delete(
                    new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, dto.getId())
            );
            if (!dto.getRoleIds().isEmpty()) {
                saveUserRoles(dto.getId(), dto.getRoleIds());
            }
        }

        SysUserVO vo = sysUserConverter.pojoToVO(sysUser);
        vo.setRoleIds(dto.getRoleIds() != null ? dto.getRoleIds() : getRoleIdsByUserId(dto.getId()));
        return ResponseDTO.success(vo);
    }

    @Override
    public ResponseDTO<String> deleteUser(Long id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        if (sysUser == null) {
            return ResponseDTO.fail("用户不存在");
        }
        sysUserMapper.deleteById(id);
        return ResponseDTO.success("删除成功");
    }

    @Override
    public ResponseDTO<String> resetPassword(Long id, String newPassword) {
        SysUser sysUser = sysUserMapper.selectById(id);
        if (sysUser == null) {
            return ResponseDTO.fail("用户不存在");
        }
        // SM2 解密前端传来的新密码，SM3 带盐值哈希存储
        String plainPassword = SM2Util.decrypt(newPassword, sm2PrivateKey);
        String salt = UUID.randomUUID().toString().replace("-", "");
        sysUser.setPassword(SM3Util.digestWithSalt(plainPassword, salt));
        sysUser.setSalt(salt);
        sysUserMapper.updateById(sysUser);
        return ResponseDTO.success("密码重置成功");
    }

    @Override
    public ResponseDTO<List<SysRole>> getUserRoles(Long userId) {
        List<SysUserRole> userRoles = sysUserRoleMapper.selectList(
                new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, userId)
        );
        List<Long> roleIds = userRoles.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
        if (roleIds.isEmpty()) {
            return ResponseDTO.success(new ArrayList<>());
        }
        List<SysRole> roles = sysRoleMapper.selectBatchIds(roleIds);
        return ResponseDTO.success(roles);
    }

    @Override
    public ResponseDTO<LoginSysUserVO> refreshToken(String refreshTokenStr) {
        if (!jwtUtils.validateRefreshToken(refreshTokenStr)) {
            return ResponseDTO.fail("Refresh Token 无效或已过期");
        }
        Map<String, Object> claims = jwtUtils.getUserInfoFromRefreshToken(refreshTokenStr);
        if (claims == null) {
            return ResponseDTO.fail("Refresh Token 无效");
        }
        Long userId = Long.parseLong(String.valueOf(claims.get("userId")));
        Long roleId = Long.parseLong(String.valueOf(claims.getOrDefault("roleId", 0)));

        String newAccessToken = jwtUtils.createAccessToken(userId, roleId);
        String newRefreshToken = jwtUtils.createRefreshToken(userId, roleId);

        SysUser sysUser = sysUserMapper.selectById(userId);
        if (sysUser == null) {
            return ResponseDTO.fail("用户不存在");
        }
        if (sysUser.getStatus() != 1) {
            return ResponseDTO.fail("账号已被禁用");
        }

        SysUserVO userVO = sysUserConverter.pojoToVO(sysUser);
        userVO.setRoleIds(getRoleIdsByUserId(userId));

        return ResponseDTO.success(new LoginSysUserVO(newAccessToken, newRefreshToken, userVO));
    }

    private List<Long> getRoleIdsByUserId(Long userId) {
        List<SysUserRole> userRoles = sysUserRoleMapper.selectList(
                new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, userId)
        );
        return userRoles.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
    }

    private void saveUserRoles(Long userId, List<Long> roleIds) {
        for (Long roleId : roleIds) {
            SysUserRole userRole = new SysUserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);
            sysUserRoleMapper.insert(userRole);
        }
    }
}
