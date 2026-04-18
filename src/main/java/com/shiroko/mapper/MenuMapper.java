package com.shiroko.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shiroko.repository.dto.QueryMenuDTO;
import com.shiroko.repository.entity.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * Description: 菜单数据库接口类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/4/18 上午15:21
 */
public interface MenuMapper extends BaseMapper<Menu> {

    IPage<Menu> getMenuByRole(IPage<Menu> page, QueryMenuDTO queryMenuDTO);

}




