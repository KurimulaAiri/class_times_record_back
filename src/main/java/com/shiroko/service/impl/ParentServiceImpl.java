package com.shiroko.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiroko.mapper.ParentMapper;
import com.shiroko.repository.entity.Parent;
import com.shiroko.service.ParentService;
import org.springframework.stereotype.Service;

/**
 * Description: 家长类Service实现类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/4/24 下午21:24
 */
@Service("parentService")
public class ParentServiceImpl extends ServiceImpl<ParentMapper, Parent>
    implements ParentService{

}




