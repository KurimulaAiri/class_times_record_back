package com.shiroko.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiroko.converter.AdminConverter;
import com.shiroko.converter.UserConverter;
import com.shiroko.mapper.UserMapper;
import com.shiroko.repository.entity.Admin;
import com.shiroko.repository.entity.Permission;
import com.shiroko.repository.entity.User;
import com.shiroko.repository.entity.UserPlatform;
import com.shiroko.repository.entity.common.RoleBaseEntity;
import com.shiroko.repository.vo.UserVO;
import com.shiroko.service.AdminService;
import com.shiroko.service.IdentityService;
import com.shiroko.service.UserPlatformService;
import com.shiroko.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final AdminService adminService;
    private final UserPlatformService userPlatformService;
    private final IdentityService identityService;

    private final AdminConverter adminConverter;
    private final UserConverter userConverter;

    private final UserMapper userMapper;

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

        RoleBaseEntity roleEntity = identityService.getByUserId(permissionName, user.getId());

        UserVO<RoleBaseEntity> vo = userConverter.pojoToVO(user);
        vo.setRoleId(permission.getId());
        vo.setIdentityInfo(roleEntity);

        Admin admin = adminService.getOne(new LambdaQueryWrapper<Admin>().eq(Admin::getUserId, user.getId()));
        vo.setAdmin(adminConverter.pojoToVO(admin));
        return vo;
    }
}
