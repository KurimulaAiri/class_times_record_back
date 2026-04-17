package com.shiroko.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiroko.repository.entity.Permission;
import com.shiroko.service.PermissionService;
import com.shiroko.mapper.PermissionMapper;
import org.springframework.stereotype.Service;

/**
 * Description: 权限服务类实现
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/4/17 下午22:30
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission>
    implements PermissionService{

}




