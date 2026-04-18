package com.shiroko.controller;

import com.shiroko.repository.dto.QueryMenuDTO;
import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.vo.QueryMenuVO;
import com.shiroko.service.MenuService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description: 菜单控制器类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/4/18 下午3:25
 */
@RestController
@RequestMapping("/menu")
public class MenuController {

    private final MenuService menuService;

    private MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping("/get_menu_by_role")
    public ResponseDTO<QueryMenuVO> getMenuByRole(@RequestBody QueryMenuDTO queryMenuDTO) {
        return ResponseDTO.success(menuService.getMenuByRole(queryMenuDTO));
    }
}
