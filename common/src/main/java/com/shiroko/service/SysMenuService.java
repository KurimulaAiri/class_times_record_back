package com.shiroko.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.dto.admin.InsertSysMenuDTO;
import com.shiroko.repository.dto.admin.QuerySysMenuDTO;
import com.shiroko.repository.dto.admin.UpdateSysMenuDTO;
import com.shiroko.repository.entity.SysMenu;
import com.shiroko.repository.vo.admin.SysMenuVO;

import java.util.List;

public interface SysMenuService extends IService<SysMenu> {

    ResponseDTO<List<SysMenuVO>> listMenus(QuerySysMenuDTO dto);

    ResponseDTO<List<SysMenuVO>> getMenuTree();

    ResponseDTO<SysMenuVO> insertMenu(InsertSysMenuDTO dto);

    ResponseDTO<SysMenuVO> updateMenu(UpdateSysMenuDTO dto);

    ResponseDTO<String> deleteMenu(Long id);

    ResponseDTO<List<SysMenu>> getMenusByRoleId(Long roleId);

    ResponseDTO<List<SysMenuVO>> getUserMenuTree();
}
