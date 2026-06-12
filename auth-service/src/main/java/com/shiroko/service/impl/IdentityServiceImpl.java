package com.shiroko.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shiroko.mapper.ParentMapper;
import com.shiroko.mapper.TeacherMapper;
import com.shiroko.repository.entity.Parent;
import com.shiroko.repository.entity.Teacher;
import com.shiroko.repository.entity.common.RoleBaseEntity;
import com.shiroko.service.IdentityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 身份查询服务实现 - 直接操作角色表，替代 Feign 跨服务调用
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class IdentityServiceImpl implements IdentityService {

    private final TeacherMapper teacherMapper;
    private final ParentMapper parentMapper;

    @Override
    public RoleBaseEntity getByUserId(String roleName, Long userId) {
        return switch (roleName.toLowerCase()) {
            case "teacher" -> {
                QueryWrapper<Teacher> wrapper = new QueryWrapper<>();
                wrapper.eq("user_id", userId);
                yield teacherMapper.selectOne(wrapper);
            }
            case "parent" -> {
                QueryWrapper<Parent> wrapper = new QueryWrapper<>();
                wrapper.eq("user_id", userId);
                yield parentMapper.selectOne(wrapper);
            }
            default -> throw new IllegalArgumentException("不支持的角色: " + roleName);
        };
    }

    @Override
    public boolean checkAvailable(String roleName, Long userId) {
        return switch (roleName.toLowerCase()) {
            case "teacher" -> {
                QueryWrapper<Teacher> wrapper = new QueryWrapper<>();
                wrapper.eq("user_id", userId).eq("is_available", true);
                yield teacherMapper.selectCount(wrapper) > 0;
            }
            case "parent" -> {
                QueryWrapper<Parent> wrapper = new QueryWrapper<>();
                wrapper.eq("user_id", userId).eq("is_available", true);
                yield parentMapper.selectCount(wrapper) > 0;
            }
            default -> false;
        };
    }

    @Override
    public void createIdentity(String roleName, Long userId) {
        switch (roleName.toLowerCase()) {
            case "teacher" -> {
                Teacher teacher = new Teacher();
                teacher.setUserId(userId);
                teacher.setIsAvailable(true);
                teacherMapper.insert(teacher);
            }
            case "parent" -> {
                Parent parent = new Parent();
                parent.setUserId(userId);
                parent.setIsAvailable(true);
                parentMapper.insert(parent);
            }
            default -> throw new IllegalArgumentException("不支持的角色: " + roleName);
        }
    }
}
