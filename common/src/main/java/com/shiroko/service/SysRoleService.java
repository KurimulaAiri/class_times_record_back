package com.shiroko.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.dto.admin.InsertSysRoleDTO;
import com.shiroko.repository.dto.admin.QuerySysRoleDTO;
import com.shiroko.repository.dto.admin.UpdateSysRoleDTO;
import com.shiroko.repository.entity.SysMenu;
import com.shiroko.repository.entity.SysRole;
import com.shiroko.repository.vo.admin.SysRoleVO;

import java.util.List;
import java.util.Map;

public interface SysRoleService extends IService<SysRole> {

    ResponseDTO<Map<String, Object>> listRoles(QuerySysRoleDTO dto);

    ResponseDTO<SysRoleVO> getRoleById(Long id);

    ResponseDTO<SysRoleVO> insertRole(InsertSysRoleDTO dto);

    ResponseDTO<SysRoleVO> updateRole(UpdateSysRoleDTO dto);

    ResponseDTO<String> deleteRole(Long id);

    ResponseDTO<List<SysMenu>> getRoleMenus(Long roleId);

    ResponseDTO<String> saveRoleMenus(Long roleId, List<Long> menuIds);
}
