package com.shiroko.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shiroko.annotation.OperationLog;
import com.shiroko.converter.SysOperationLogConverter;
import com.shiroko.mapper.SysOperationLogMapper;
import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.entity.SysOperationLog;
import com.shiroko.repository.vo.admin.SysOperationLogVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/operation_log")
@RequiredArgsConstructor
public class SysOperationLogController {

    private final SysOperationLogMapper sysOperationLogMapper;
    private final SysOperationLogConverter sysOperationLogConverter;

    @PostMapping("/list")
    public ResponseDTO<Map<String, Object>> getLogList(@RequestBody Map<String, Object> params) {
        int currentPage = params.get("currentPage") != null ? Integer.parseInt(params.get("currentPage").toString()) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 10;
        String operation = params.get("operation") != null ? params.get("operation").toString() : null;
        String username = params.get("username") != null ? params.get("username").toString() : null;

        LambdaQueryWrapper<SysOperationLog> wrapper = new LambdaQueryWrapper<>();
        if (operation != null && !operation.isEmpty()) {
            wrapper.like(SysOperationLog::getOperation, operation);
        }
        if (username != null && !username.isEmpty()) {
            wrapper.like(SysOperationLog::getUsername, username);
        }
        wrapper.orderByDesc(SysOperationLog::getCreateTime);

        IPage<SysOperationLog> page = sysOperationLogMapper.selectPage(new Page<>(currentPage, pageSize), wrapper);

        List<SysOperationLogVO> voList = page.getRecords().stream()
                .map(sysOperationLogConverter::pojoToVO)
                .collect(Collectors.toList());

        Map<String, Object> data = new HashMap<>();
        data.put("list", voList);
        data.put("total", page.getTotal());
        return ResponseDTO.success(data);
    }

    @PostMapping("/delete")
    @OperationLog("删除操作日志")
    public ResponseDTO<String> deleteLog(@RequestBody Map<String, Object> params) {
        Long id = Long.parseLong(params.get("id").toString());
        sysOperationLogMapper.deleteById(id);
        return ResponseDTO.success("删除成功");
    }

    @PostMapping("/clear")
    @OperationLog("清空操作日志")
    public ResponseDTO<String> clearLogs() {
        sysOperationLogMapper.delete(null);
        return ResponseDTO.success("清空成功");
    }
}
