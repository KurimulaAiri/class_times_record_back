package com.shiroko.controller;

import com.shiroko.annotation.OperationLog;
import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.dto.admin.InsertSysMenuDTO;
import com.shiroko.repository.dto.admin.QuerySysMenuDTO;
import com.shiroko.repository.dto.admin.UpdateSysMenuDTO;
import com.shiroko.repository.entity.SysMenu;
import com.shiroko.repository.vo.admin.SysMenuVO;
import com.shiroko.service.SysMenuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/menu")
@RequiredArgsConstructor
public class SysMenuController {

    private final SysMenuService sysMenuService;

    @PostMapping("/list")
    public ResponseDTO<List<SysMenuVO>> listMenus(@RequestBody QuerySysMenuDTO dto) {
        return sysMenuService.listMenus(dto);
    }

    @PostMapping("/tree")
    public ResponseDTO<List<SysMenuVO>> getMenuTree() {
        return sysMenuService.getMenuTree();
    }

    @PostMapping("/user_tree")
    public ResponseDTO<List<SysMenuVO>> getUserMenuTree() {
        return sysMenuService.getUserMenuTree();
    }

    @PostMapping("/insert")
    public ResponseDTO<SysMenuVO> insertMenu(@Valid @RequestBody InsertSysMenuDTO dto) {
        return sysMenuService.insertMenu(dto);
    }

    @PostMapping("/update")
    @OperationLog("更新菜单")
    public ResponseDTO<SysMenuVO> updateMenu(@Valid @RequestBody UpdateSysMenuDTO dto) {
        return sysMenuService.updateMenu(dto);
    }

    @PostMapping("/delete")
    public ResponseDTO<String> deleteMenu(@RequestBody Map<String, Long> params) {
        return sysMenuService.deleteMenu(params.get("id"));
    }
}
