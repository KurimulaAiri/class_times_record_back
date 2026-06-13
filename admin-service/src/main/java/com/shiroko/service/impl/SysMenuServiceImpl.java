package com.shiroko.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiroko.converter.SysMenuConverter;
import com.shiroko.context.UserContext;
import com.shiroko.mapper.SysMenuMapper;
import com.shiroko.mapper.SysRoleMenuMapper;
import com.shiroko.mapper.SysUserRoleMapper;
import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.dto.UserDTO;
import com.shiroko.repository.dto.admin.InsertSysMenuDTO;
import com.shiroko.repository.dto.admin.QuerySysMenuDTO;
import com.shiroko.repository.dto.admin.UpdateSysMenuDTO;
import com.shiroko.repository.entity.SysMenu;
import com.shiroko.repository.entity.SysRoleMenu;
import com.shiroko.repository.entity.SysUserRole;
import com.shiroko.repository.vo.admin.SysMenuVO;
import com.shiroko.service.SysMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    private final SysMenuMapper sysMenuMapper;
    private final SysRoleMenuMapper sysRoleMenuMapper;
    private final SysUserRoleMapper sysUserRoleMapper;
    private final SysMenuConverter sysMenuConverter;

    @Override
    public ResponseDTO<List<SysMenuVO>> listMenus(QuerySysMenuDTO dto) {
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        if (dto.getMenuName() != null && !dto.getMenuName().isEmpty()) {
            wrapper.like(SysMenu::getMenuName, dto.getMenuName());
        }
        if (dto.getMenuType() != null && !dto.getMenuType().isEmpty()) {
            wrapper.eq(SysMenu::getMenuType, dto.getMenuType());
        }
        if (dto.getStatus() != null) {
            wrapper.eq(SysMenu::getStatus, dto.getStatus());
        }
        wrapper.orderByAsc(SysMenu::getSort);

        List<SysMenu> menus = sysMenuMapper.selectList(wrapper);
        List<SysMenuVO> voList = sysMenuConverter.pojoListToVOList(menus);
        return ResponseDTO.success(voList);
    }

    @Override
    public ResponseDTO<List<SysMenuVO>> getMenuTree() {
        List<SysMenu> allMenus = sysMenuMapper.selectList(
                new LambdaQueryWrapper<SysMenu>().orderByAsc(SysMenu::getSort)
        );
        List<SysMenuVO> allVOs = allMenus.stream()
                .map(sysMenuConverter::pojoToVO)
                .collect(Collectors.toList());

        List<SysMenuVO> tree = buildMenuTree(allVOs, 0L);
        return ResponseDTO.success(tree);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<SysMenuVO> insertMenu(InsertSysMenuDTO dto) {
        SysMenu sysMenu = new SysMenu();
        sysMenu.setParentId(dto.getParentId() != null ? dto.getParentId() : 0L);
        sysMenu.setMenuName(dto.getMenuName());
        sysMenu.setMenuType(dto.getMenuType());
        sysMenu.setPath(dto.getPath());
        sysMenu.setComponent(dto.getComponent());
        sysMenu.setPerms(dto.getPerms());
        sysMenu.setIcon(dto.getIcon());
        sysMenu.setSort(dto.getSort() != null ? dto.getSort() : 0);
        sysMenu.setStatus(dto.getStatus() != null ? dto.getStatus() : 1);

        sysMenuMapper.insert(sysMenu);

        SysMenuVO vo = sysMenuConverter.pojoToVO(sysMenu);
        return ResponseDTO.success(vo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<SysMenuVO> updateMenu(UpdateSysMenuDTO dto) {
        SysMenu sysMenu = sysMenuMapper.selectById(dto.getId());
        if (sysMenu == null) {
            return ResponseDTO.fail("菜单不存在");
        }

        if (dto.getParentId() != null) {
            sysMenu.setParentId(dto.getParentId());
        }
        if (dto.getMenuName() != null) {
            sysMenu.setMenuName(dto.getMenuName());
        }
        if (dto.getMenuType() != null) {
            sysMenu.setMenuType(dto.getMenuType());
        }
        if (dto.getPath() != null) {
            sysMenu.setPath(dto.getPath());
        }
        if (dto.getComponent() != null) {
            sysMenu.setComponent(dto.getComponent());
        }
        if (dto.getPerms() != null) {
            sysMenu.setPerms(dto.getPerms());
        }
        if (dto.getIcon() != null) {
            sysMenu.setIcon(dto.getIcon());
        }
        if (dto.getSort() != null) {
            sysMenu.setSort(dto.getSort());
        }
        if (dto.getStatus() != null) {
            sysMenu.setStatus(dto.getStatus());
        }

        sysMenuMapper.updateById(sysMenu);

        SysMenuVO vo = sysMenuConverter.pojoToVO(sysMenu);
        return ResponseDTO.success(vo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> deleteMenu(Long id) {
        SysMenu sysMenu = sysMenuMapper.selectById(id);
        if (sysMenu == null) {
            return ResponseDTO.fail("菜单不存在");
        }

        // Check if menu has children
        Long childCount = sysMenuMapper.selectCount(
                new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getParentId, id)
        );
        if (childCount > 0) {
            return ResponseDTO.fail("存在子菜单，无法删除");
        }

        sysMenuMapper.deleteById(id);

        // Also delete role-menu associations
        sysRoleMenuMapper.delete(
                new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getMenuId, id)
        );

        return ResponseDTO.success("删除成功");
    }

    @Override
    public ResponseDTO<List<SysMenu>> getMenusByRoleId(Long roleId) {
        List<SysRoleMenu> roleMenus = sysRoleMenuMapper.selectList(
                new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, roleId)
        );
        List<Long> menuIds = roleMenus.stream().map(SysRoleMenu::getMenuId).collect(Collectors.toList());
        if (menuIds.isEmpty()) {
            return ResponseDTO.success(List.of());
        }
        List<SysMenu> menus = sysMenuMapper.selectBatchIds(menuIds);
        return ResponseDTO.success(menus);
    }

    @Override
    public ResponseDTO<List<SysMenuVO>> getUserMenuTree() {
        UserDTO user = UserContext.getUser();
        Long userId = user.getId();

        // 查询用户角色ID列表
        List<SysUserRole> userRoles = sysUserRoleMapper.selectList(
                new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, userId)
        );
        List<Long> roleIds = userRoles.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
        if (roleIds.isEmpty()) {
            return ResponseDTO.success(List.of());
        }

        // 查询角色关联的菜单ID
        List<SysRoleMenu> roleMenus = sysRoleMenuMapper.selectList(
                new LambdaQueryWrapper<SysRoleMenu>().in(SysRoleMenu::getRoleId, roleIds)
        );
        List<Long> menuIds = roleMenus.stream().map(SysRoleMenu::getMenuId).distinct().collect(Collectors.toList());
        if (menuIds.isEmpty()) {
            return ResponseDTO.success(List.of());
        }

        // 查询菜单实体，过滤 status=1 且 menuType 为 M、C 或 L，按 sort 升序
        List<SysMenu> menus = sysMenuMapper.selectList(
                new LambdaQueryWrapper<SysMenu>()
                        .in(SysMenu::getId, menuIds)
                        .eq(SysMenu::getStatus, 1)
                        .in(SysMenu::getMenuType, "M", "C", "L")
                        .orderByAsc(SysMenu::getSort)
        );

        List<SysMenuVO> voList = menus.stream()
                .map(sysMenuConverter::pojoToVO)
                .collect(Collectors.toList());

        List<SysMenuVO> tree = buildMenuTree(voList, 0L);
        return ResponseDTO.success(tree);
    }

    private List<SysMenuVO> buildMenuTree(List<SysMenuVO> allMenus, Long parentId) {
        List<SysMenuVO> tree = new ArrayList<>();
        for (SysMenuVO menu : allMenus) {
            if (parentId.equals(menu.getParentId())) {
                menu.setChildren(buildMenuTree(allMenus, menu.getId()));
                tree.add(menu);
            }
        }
        return tree;
    }
}
