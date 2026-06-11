package com.shiroko.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiroko.converter.SysRoleConverter;
import com.shiroko.mapper.SysMenuMapper;
import com.shiroko.mapper.SysRoleMapper;
import com.shiroko.mapper.SysRoleMenuMapper;
import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.dto.admin.InsertSysRoleDTO;
import com.shiroko.repository.dto.admin.QuerySysRoleDTO;
import com.shiroko.repository.dto.admin.UpdateSysRoleDTO;
import com.shiroko.repository.entity.SysMenu;
import com.shiroko.repository.entity.SysRole;
import com.shiroko.repository.entity.SysRoleMenu;
import com.shiroko.repository.vo.admin.SysRoleVO;
import com.shiroko.service.SysRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    private final SysRoleMapper sysRoleMapper;
    private final SysRoleMenuMapper sysRoleMenuMapper;
    private final SysMenuMapper sysMenuMapper;
    private final SysRoleConverter sysRoleConverter;

    @Override
    public ResponseDTO<Map<String, Object>> listRoles(QuerySysRoleDTO dto) {
        Page<SysRole> page = new Page<>(dto.getCurrentPage(), dto.getPageSize());

        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        if (dto.getRoleName() != null && !dto.getRoleName().isEmpty()) {
            wrapper.like(SysRole::getRoleName, dto.getRoleName());
        }
        if (dto.getRoleKey() != null && !dto.getRoleKey().isEmpty()) {
            wrapper.like(SysRole::getRoleKey, dto.getRoleKey());
        }
        if (dto.getStatus() != null) {
            wrapper.eq(SysRole::getStatus, dto.getStatus());
        }
        wrapper.orderByAsc(SysRole::getSort);

        Page<SysRole> result = sysRoleMapper.selectPage(page, wrapper);

        List<SysRoleVO> voList = result.getRecords().stream().map(role -> {
            SysRoleVO vo = sysRoleConverter.pojoToVO(role);
            vo.setMenuIds(getMenuIdsByRoleId(role.getId()));
            return vo;
        }).collect(Collectors.toList());

        Map<String, Object> data = new HashMap<>();
        data.put("list", voList);
        data.put("total", result.getTotal());
        data.put("currentPage", result.getCurrent());
        data.put("pageSize", result.getSize());

        return ResponseDTO.success(data);
    }

    @Override
    public ResponseDTO<SysRoleVO> getRoleById(Long id) {
        SysRole sysRole = sysRoleMapper.selectById(id);
        if (sysRole == null) {
            return ResponseDTO.fail("角色不存在");
        }
        SysRoleVO vo = sysRoleConverter.pojoToVO(sysRole);
        vo.setMenuIds(getMenuIdsByRoleId(id));
        return ResponseDTO.success(vo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<SysRoleVO> insertRole(InsertSysRoleDTO dto) {
        // Check roleKey uniqueness
        Long count = sysRoleMapper.selectCount(
                new LambdaQueryWrapper<SysRole>().eq(SysRole::getRoleKey, dto.getRoleKey())
        );
        if (count > 0) {
            return ResponseDTO.fail("角色标识已存在");
        }

        SysRole sysRole = new SysRole();
        sysRole.setRoleName(dto.getRoleName());
        sysRole.setRoleKey(dto.getRoleKey());
        sysRole.setSort(dto.getSort() != null ? dto.getSort() : 0);
        sysRole.setStatus(dto.getStatus() != null ? dto.getStatus() : 1);
        sysRole.setRemark(dto.getRemark());

        sysRoleMapper.insert(sysRole);

        // Assign menus
        if (dto.getMenuIds() != null && !dto.getMenuIds().isEmpty()) {
            saveRoleMenus(sysRole.getId(), dto.getMenuIds());
        }

        SysRoleVO vo = sysRoleConverter.pojoToVO(sysRole);
        vo.setMenuIds(dto.getMenuIds());
        return ResponseDTO.success(vo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<SysRoleVO> updateRole(UpdateSysRoleDTO dto) {
        SysRole sysRole = sysRoleMapper.selectById(dto.getId());
        if (sysRole == null) {
            return ResponseDTO.fail("角色不存在");
        }

        if (dto.getRoleName() != null) {
            sysRole.setRoleName(dto.getRoleName());
        }
        if (dto.getRoleKey() != null) {
            // Check roleKey uniqueness if changing
            Long count = sysRoleMapper.selectCount(
                    new LambdaQueryWrapper<SysRole>()
                            .eq(SysRole::getRoleKey, dto.getRoleKey())
                            .ne(SysRole::getId, dto.getId())
            );
            if (count > 0) {
                return ResponseDTO.fail("角色标识已存在");
            }
            sysRole.setRoleKey(dto.getRoleKey());
        }
        if (dto.getSort() != null) {
            sysRole.setSort(dto.getSort());
        }
        if (dto.getStatus() != null) {
            sysRole.setStatus(dto.getStatus());
        }
        if (dto.getRemark() != null) {
            sysRole.setRemark(dto.getRemark());
        }

        sysRoleMapper.updateById(sysRole);

        // Reassign menus
        if (dto.getMenuIds() != null) {
            sysRoleMenuMapper.delete(
                    new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, dto.getId())
            );
            if (!dto.getMenuIds().isEmpty()) {
                saveRoleMenus(dto.getId(), dto.getMenuIds());
            }
        }

        SysRoleVO vo = sysRoleConverter.pojoToVO(sysRole);
        vo.setMenuIds(dto.getMenuIds() != null ? dto.getMenuIds() : getMenuIdsByRoleId(dto.getId()));
        return ResponseDTO.success(vo);
    }

    @Override
    public ResponseDTO<String> deleteRole(Long id) {
        SysRole sysRole = sysRoleMapper.selectById(id);
        if (sysRole == null) {
            return ResponseDTO.fail("角色不存在");
        }
        sysRoleMapper.deleteById(id);
        // Also delete role-menu associations
        sysRoleMenuMapper.delete(
                new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, id)
        );
        return ResponseDTO.success("删除成功");
    }

    @Override
    public ResponseDTO<List<SysMenu>> getRoleMenus(Long roleId) {
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

    private List<Long> getMenuIdsByRoleId(Long roleId) {
        List<SysRoleMenu> roleMenus = sysRoleMenuMapper.selectList(
                new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, roleId)
        );
        return roleMenus.stream().map(SysRoleMenu::getMenuId).collect(Collectors.toList());
    }

    private void saveRoleMenus(Long roleId, List<Long> menuIds) {
        for (Long menuId : menuIds) {
            SysRoleMenu roleMenu = new SysRoleMenu();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(menuId);
            sysRoleMenuMapper.insert(roleMenu);
        }
    }
}
