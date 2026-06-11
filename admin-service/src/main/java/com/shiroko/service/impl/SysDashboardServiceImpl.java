package com.shiroko.service.impl;

import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.vo.admin.DashboardVO;
import com.shiroko.service.SysDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SysDashboardServiceImpl implements SysDashboardService {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public ResponseDTO<DashboardVO> getDashboardData() {
        Long studentCount = countTable("student");
        Long teacherCount = countTable("teacher");
        Long institutionCount = countTable("institution");
        Long courseCount = countTable("course");
        Long classCount = countTable("`class`");

        return ResponseDTO.success(new DashboardVO(studentCount, teacherCount, institutionCount, courseCount, classCount));
    }

    private Long countTable(String tableName) {
        try {
            return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM " + tableName + " WHERE is_deleted = 0", Long.class);
        } catch (Exception e) {
            return 0L;
        }
    }
}
