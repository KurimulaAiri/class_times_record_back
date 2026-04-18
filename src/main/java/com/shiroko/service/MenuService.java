package com.shiroko.service;

import com.shiroko.repository.dto.QueryMenuDTO;
import com.shiroko.repository.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shiroko.repository.vo.QueryMenuVO;

/**
 * Description: 菜单服务接口
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/4/18 上午15:21
 */
public interface MenuService extends IService<Menu> {

    QueryMenuVO getMenuByRole(QueryMenuDTO queryMenuDTO);
}
