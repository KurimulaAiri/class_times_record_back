package com.shiroko.controller;

import com.shiroko.annotation.OperationLog;
import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.dto.admin.InsertSysRoleDTO;
import com.shiroko.repository.dto.admin.QuerySysRoleDTO;
import com.shiroko.repository.dto.admin.UpdateSysRoleDTO;
import com.shiroko.repository.entity.SysMenu;
import com.shiroko.repository.vo.admin.SysRoleVO;
import com.shiroko.service.SysRoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
public class SysRoleController {

    private final SysRoleService sysRoleService;

    @PostMapping("/list")
    public ResponseDTO<Map<String, Object>> listRoles(@RequestBody QuerySysRoleDTO dto) {
        return sysRoleService.listRoles(dto);
    }

    @PostMapping("/get_by_id")
    public ResponseDTO<SysRoleVO> getRoleById(@RequestBody Map<String, Long> params) {
        return sysRoleService.getRoleById(params.get("id"));
    }

    @PostMapping("/insert")
    public ResponseDTO<SysRoleVO> insertRole(@Valid @RequestBody InsertSysRoleDTO dto) {
        return sysRoleService.insertRole(dto);
    }

    @PostMapping("/update")
    public ResponseDTO<SysRoleVO> updateRole(@Valid @RequestBody UpdateSysRoleDTO dto) {
        return sysRoleService.updateRole(dto);
    }

    @PostMapping("/delete")
    @OperationLog("删除角色")
    public ResponseDTO<String> deleteRole(@RequestBody Map<String, Long> params) {
        return sysRoleService.deleteRole(params.get("id"));
    }

    @PostMapping("/get_menus")
    public ResponseDTO<List<SysMenu>> getRoleMenus(@RequestBody Map<String, Long> params) {
        return sysRoleService.getRoleMenus(params.get("roleId"));
    }

    @PostMapping("/save_menus")
    @OperationLog("保存角色菜单权限")
    public ResponseDTO<String> saveRoleMenus(@RequestBody Map<String, Object> params) {
        Long roleId = Long.parseLong(params.get("roleId").toString());
        @SuppressWarnings("unchecked")
        List<Integer> menuIds = (List<Integer>) params.get("menuIds");
        List<Long> menuIdList = menuIds.stream().map(Long::valueOf).collect(java.util.stream.Collectors.toList());
        return sysRoleService.saveRoleMenus(roleId, menuIdList);
    }
}
