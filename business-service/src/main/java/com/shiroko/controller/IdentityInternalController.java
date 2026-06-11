package com.shiroko.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.entity.common.RoleBaseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 内部接口：供 auth-service 通过 Feign 调用的角色身份查询
 */
@RestController
@RequestMapping("/internal/identity")
@RequiredArgsConstructor
public class IdentityInternalController {

    private final Map<String, IService<? extends RoleBaseEntity>> identityServiceMap;
    private final ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();

    @PostMapping("/get_by_user_id")
    public ResponseDTO<Map<String, Object>> getByIdentity(@RequestBody Map<String, Object> params) {
        String roleName = (String) params.get("roleName");
        Long userId = Long.valueOf(params.get("userId").toString());

        IService<? extends RoleBaseEntity> service = identityServiceMap.get(roleName + "Service");
        if (service == null) {
            return ResponseDTO.fail("未找到对应的角色服务: " + roleName);
        }

        RoleBaseEntity entity = queryByUserId(service, userId);
        if (entity == null) {
            return ResponseDTO.success(null);
        }
        Map<String, Object> entityMap = objectMapper.convertValue(entity, Map.class);
        return ResponseDTO.success(entityMap);
    }

    /**
     * 检查身份是否可用（存在且 is_available=true）
     */
    @PostMapping("/check_available")
    public ResponseDTO<Boolean> checkAvailable(@RequestBody Map<String, Object> params) {
        String roleName = (String) params.get("roleName");
        Long userId = Long.valueOf(params.get("userId").toString());

        IService<? extends RoleBaseEntity> service = identityServiceMap.get(roleName + "Service");
        if (service == null) {
            return ResponseDTO.success(false);
        }

        long count = countAvailable(service, userId);
        return ResponseDTO.success(count > 0);
    }

    /**
     * 创建身份记录（注册时使用）
     */
    @PostMapping("/create")
    public ResponseDTO<String> createIdentity(@RequestBody Map<String, Object> params) {
        String roleName = (String) params.get("roleName");
        Long userId = Long.valueOf(params.get("userId").toString());
        Long role = Long.valueOf(params.get("role").toString());

        IService<? extends RoleBaseEntity> service = identityServiceMap.get(roleName + "Service");
        if (service == null) {
            return ResponseDTO.fail("未找到对应的角色服务: " + roleName);
        }

        try {
            createIdentityRecord(service, userId);
            return ResponseDTO.success("身份记录创建成功");
        } catch (Exception e) {
            return ResponseDTO.fail("身份记录创建失败: " + e.getMessage());
        }
    }

    /**
     * 辅助方法：捕获通配符泛型，避免编译错误
     */
    private <T extends RoleBaseEntity> RoleBaseEntity queryByUserId(IService<T> service, Long userId) {
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        return service.getOne(wrapper);
    }

    private <T extends RoleBaseEntity> long countAvailable(IService<T> service, Long userId) {
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.eq("is_available", true);
        return service.count(wrapper);
    }

    private <T extends RoleBaseEntity> void createIdentityRecord(IService<T> service, Long userId) throws Exception {
        Class<T> entityClass = service.getEntityClass();
        T entity = entityClass.getDeclaredConstructor().newInstance();

        java.lang.reflect.Field userIdField = ReflectionUtils.findField(entityClass, "userId");
        ReflectionUtils.makeAccessible(userIdField);
        ReflectionUtils.setField(userIdField, entity, userId);

        service.save(entity);
    }
}
