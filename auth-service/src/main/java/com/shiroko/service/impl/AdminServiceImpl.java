package com.shiroko.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiroko.mapper.AdminMapper;
import com.shiroko.repository.entity.Admin;
import com.shiroko.service.AdminService;
import org.springframework.stereotype.Service;

/**
 * Description: 管理员实体类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/20 上午0:21
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

}




