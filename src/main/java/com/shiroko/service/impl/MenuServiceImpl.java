package com.shiroko.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiroko.context.UserContext;
import com.shiroko.converter.MenuConverter;
import com.shiroko.repository.dto.QueryMenuDTO;
import com.shiroko.repository.entity.Menu;
import com.shiroko.repository.vo.MenuVO;
import com.shiroko.repository.vo.QueryMenuVO;
import com.shiroko.service.MenuService;
import com.shiroko.mapper.MenuMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description: 菜单服务接口实现
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/4/18 上午15:21
 */
@Service
@RequiredArgsConstructor
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    private final MenuMapper menuMapper;

    private final MenuConverter menuConverter;

    @Override
    public QueryMenuVO getMenuByRole(QueryMenuDTO queryMenuDTO) {
        queryMenuDTO.setRoleId(UserContext.getUser().getRoleId());
        IPage<Menu> page = new Page<>(queryMenuDTO.getCurrentPage(), queryMenuDTO.getPageSize());
        List<MenuVO> menuVOs = menuConverter.pojoListToVOList(menuMapper.getMenuByRole(page, queryMenuDTO).getRecords());
        return new QueryMenuVO(menuVOs, page.getTotal());
    }
}




