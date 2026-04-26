package com.shiroko.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiroko.converter.UserConverter;
import com.shiroko.mapper.UserMapper;
import com.shiroko.repository.entity.Permission;
import com.shiroko.repository.entity.RoleBaseEntity;
import com.shiroko.repository.entity.User;
import com.shiroko.repository.vo.UserVO;
import com.shiroko.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    private final UserConverter userConverter;

    private final Map<String, IService<? extends RoleBaseEntity>> identityServiceMap;

    @Override
    public Long saveOrUpdateUser(String openid) {
        User user = this.getOne(new LambdaQueryWrapper<User>().eq(User::getOpenid, openid));
        if (user == null) {
            user = new User();
            user.setOpenid(openid);
        }
        this.saveOrUpdate(user);
        return user.getId();
    }

    @Override
    public UserVO<RoleBaseEntity> getFullUserInfoByOpenid(String openid, Permission permission) {
        // 1. 获取基础用户信息
        User user = this.getOne(new LambdaQueryWrapper<User>().eq(User::getOpenid, openid));

        if (user == null) {
            return null;
        }

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

        // 5. 动态查询具体的身份实体（Teacher/Parent 等）并填充
        // 传入 user.getId()，因为身份表里存的是业务用户的 ID
        fillIdentityDetail(permissionService, vo, user.getId());

        return vo;
    }

    /**
     * 泛型辅助方法：根据 userId 在动态的业务表中查询记录
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
