package com.shiroko.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
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
import com.shiroko.service.UserPlatformService;
import com.shiroko.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * Description: 用户服务实现类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/3/19 上午23:34
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final AdminService adminService;
    private final UserPlatformService userPlatformService;

    private final AdminConverter adminConverter;
    private final UserConverter userConverter;

    private final Map<String, IService<? extends RoleBaseEntity>> identityServiceMap;
    private final UserMapper userMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserPlatform saveOrUpdateUser(String openid, String platform) {
        // 1. 先去平台表查询该平台的 openid 是否已经存在
        UserPlatform platformRecord = userPlatformService.getOne(
                new LambdaQueryWrapper<UserPlatform>()
                        .eq(UserPlatform::getPlatform, platform)
                        .eq(UserPlatform::getOpenId, openid)
        );

        if (platformRecord == null) {
            // 2. 场景：该微信从未在这台平台上出现过，为全新的微信身份
            // 先初始化主表 user（注意：此处暂时不指定 institutionId，由后续绑定或注册逻辑决定）
            User user = new User();
            this.save(user);

            // 3. 创建平台绑定关系
            platformRecord = new UserPlatform();
            platformRecord.setUserId(user.getId());
            platformRecord.setPlatform(platform);
            platformRecord.setOpenId(openid);
            platformRecord.setIsAvailable(true); // 💡 新增：新用户初始化默认可用 (1:可用, 0:禁用)

            try {
                userPlatformService.save(platformRecord);
            } catch (DuplicateKeyException e) {
                // 兜底高并发
                platformRecord = userPlatformService.getOne(
                        new LambdaQueryWrapper<UserPlatform>()
                                .eq(UserPlatform::getPlatform, platform)
                                .eq(UserPlatform::getOpenId, openid)
                );
            }
        }

        // 返回平台记录（包含 userId 和 isAvailable 状态）
        return platformRecord;
    }

    @Override
    public UserVO<RoleBaseEntity> getFullUserInfoByPlatformOpenid(String platform, String openid, Permission permission) {
        // 1. 获取基础用户信息
        User user = userMapper.selectUserByPlatformOpenid(platform, openid);

        if (user == null) {
            return null;
        }

        return getFullUserInfo(permission, user);
    }

    @Override
    public UserVO<RoleBaseEntity> getFullUserInfoByUserId(Long userId, Permission permission) {
        User user = this.getOne(new LambdaQueryWrapper<User>().eq(User::getId, userId));
        if (user == null) {
            return null;
        }

        return getFullUserInfo(permission, user);
    }

    @Override
    public UserVO<RoleBaseEntity> getFullUserInfoByPlatformOpenidAndInstitution(String platform, String openId, Long institutionId, Permission permission) {
        // 1. 获取基础用户信息
        User user = userMapper.selectUserByPlatformOpenidAndInstitution(platform, openId, institutionId);

        if (user == null) {
            return null;
        }

        return getFullUserInfo(permission, user);
    }


    private UserVO<RoleBaseEntity> getFullUserInfo(Permission permission, User user) {
        // 2. 根据权限获取对应的 Service Bean 名称
        String permissionName = permission.getPermissionName();

        // 1. 直接从 Map 中获取，无需 Character.toLowerCase 或 getBean
        // 假设 permissionName 正好是 "Student"
        IService<? extends RoleBaseEntity> permissionService = identityServiceMap.getOrDefault(permissionName + "Service", null);

        if (permissionService == null) {
            return null;
        }

        // 4. 初始化 VO 并拷贝 User 的基础信息
        UserVO<RoleBaseEntity> vo = userConverter.pojoToVO(user);

        vo.setRoleId(permission.getId());

        // 5. 动态查询具体的身份实体（Teacher/Parent 等）并填充
        // 传入 user.getId()，因为身份表里存的是业务用户的 ID
        fillIdentityDetail(permissionService, vo, user.getId());

        // 6. 检查是否是管理员
        Admin admin = adminService.getOne(new LambdaQueryWrapper<Admin>().eq(Admin::getUserId, user.getId()));
        vo.setAdmin(adminConverter.pojoToVO(admin));
        return vo;
    }

    /**
     * 泛型辅助方法：根据 userId 在动态的业务表中查询记录
     * @param service 业务表的 Service Bean
     * @param vo      用户 VO，用于填充身份信息
     * @param userId  用户 ID
     */
    private <T extends RoleBaseEntity> void fillIdentityDetail(IService<T> service, UserVO<RoleBaseEntity> vo, Long userId) {
        if (userId == null) return;

        // 关键点：因为 T 是泛型，无法使用 T::getUserId。
        // 我们使用 QueryWrapper 并直接指定列名 "user_id"（假设你的身份表该字段名统一）
        T entity = service.getOne(new QueryWrapper<T>().eq("user_id", userId));

        // 填充到 VO 的 identityInfo 字段中
        vo.setIdentityInfo(entity);
    }
}
