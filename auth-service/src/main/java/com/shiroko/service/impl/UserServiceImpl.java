package com.shiroko.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shiroko.converter.AdminConverter;
import com.shiroko.converter.UserConverter;
import com.shiroko.feign.IdentityFeignClient;
import com.shiroko.mapper.UserMapper;
import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.entity.Admin;
import com.shiroko.repository.entity.Permission;
import com.shiroko.repository.entity.User;
import com.shiroko.repository.entity.UserPlatform;
import com.shiroko.repository.entity.common.RoleBaseEntity;
import com.shiroko.repository.vo.UserVO;
import com.shiroko.service.AdminService;
import com.shiroko.service.UserPlatformService;
import com.shiroko.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final AdminService adminService;
    private final UserPlatformService userPlatformService;

    private final AdminConverter adminConverter;
    private final UserConverter userConverter;

    private final IdentityFeignClient identityFeignClient;
    private final UserMapper userMapper;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserPlatform saveOrUpdateUser(String openid, String platform) {
        UserPlatform platformRecord = userPlatformService.getOne(
                new LambdaQueryWrapper<UserPlatform>()
                        .eq(UserPlatform::getPlatform, platform)
                        .eq(UserPlatform::getOpenId, openid)
        );

        if (platformRecord == null) {
            User user = new User();
            this.save(user);

            platformRecord = new UserPlatform();
            platformRecord.setUserId(user.getId());
            platformRecord.setPlatform(platform);
            platformRecord.setOpenId(openid);
            platformRecord.setIsAvailable(true);

            try {
                userPlatformService.save(platformRecord);
            } catch (DuplicateKeyException e) {
                platformRecord = userPlatformService.getOne(
                        new LambdaQueryWrapper<UserPlatform>()
                                .eq(UserPlatform::getPlatform, platform)
                                .eq(UserPlatform::getOpenId, openid)
                );
            }
        }

        return platformRecord;
    }

    @Override
    public UserVO<RoleBaseEntity> getFullUserInfoByPlatformOpenid(String platform, String openid, Permission permission) {
        User user = userMapper.selectUserByPlatformOpenid(platform, openid);
        if (user == null) {
            return null;
        }
        return getFullUserInfo(permission, user);
    }

    @Override
    public UserVO<RoleBaseEntity> getFullUserInfoByUserId(Long userId, Permission permission) {
        User user = this.getOne(new LambdaQueryWrapper<User>().eq(User::getId, userId));
        log.debug("getFullUserInfoByUserId: {}", user);
        if (user == null) {
            return null;
        }
        return getFullUserInfo(permission, user);
    }

    @Override
    public UserVO<RoleBaseEntity> getFullUserInfoByPlatformOpenidAndInstitution(String platform, String openId, Long institutionId, Permission permission) {
        User user = userMapper.selectUserByPlatformOpenidAndInstitution(platform, openId, institutionId);
        if (user == null) {
            return null;
        }
        return getFullUserInfo(permission, user);
    }

    private UserVO<RoleBaseEntity> getFullUserInfo(Permission permission, User user) {
        String permissionName = permission.getPermissionName();

        ResponseDTO<Map<String, Object>> response = identityFeignClient.getByUserId(
                Map.of("roleName", permissionName, "userId", user.getId())
        );

        if (response.getCode() != 200 || response.getData() == null) {
            return null;
        }

        // 根据 roleName 反序列化为具体子类（Teacher / Parent），绕过抽象类 RoleBaseEntity 的反序列化
        Class<? extends RoleBaseEntity> roleClass = getRoleClass(permissionName);
        RoleBaseEntity roleEntity = objectMapper.convertValue(response.getData(), roleClass);

        UserVO<RoleBaseEntity> vo = userConverter.pojoToVO(user);
        vo.setRoleId(permission.getId());
        vo.setIdentityInfo(roleEntity);

        Admin admin = adminService.getOne(new LambdaQueryWrapper<Admin>().eq(Admin::getUserId, user.getId()));
        vo.setAdmin(adminConverter.pojoToVO(admin));
        return vo;
    }

    private Class<? extends RoleBaseEntity> getRoleClass(String roleName) {
        return switch (roleName.toLowerCase()) {
            case "teacher" -> com.shiroko.repository.entity.Teacher.class;
            case "parent" -> com.shiroko.repository.entity.Parent.class;
            default -> throw new IllegalArgumentException("Unsupported role: " + roleName);
        };
    }
}
