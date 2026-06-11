package com.shiroko.service;

import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.vo.admin.DashboardVO;

public interface SysDashboardService {

    ResponseDTO<DashboardVO> getDashboardData();
}
