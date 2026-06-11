package com.shiroko.controller;

import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.vo.admin.DashboardVO;
import com.shiroko.service.SysDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/dashboard")
@RequiredArgsConstructor
public class SysDashboardController {

    private final SysDashboardService sysDashboardService;

    @PostMapping("/data")
    public ResponseDTO<DashboardVO> getDashboardData() {
        return sysDashboardService.getDashboardData();
    }
}
