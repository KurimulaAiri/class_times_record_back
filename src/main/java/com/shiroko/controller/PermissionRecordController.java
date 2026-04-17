package com.shiroko.controller;

import com.shiroko.repository.dto.BindPermissionRecordDTO;
import com.shiroko.repository.dto.QueryPermissionRecordDTO;
import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.vo.QueryPermissionRecordVO;
import com.shiroko.service.PermissionRecordService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description: 用户权限记录Controller
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/3/24 上午12:54
 */
@RestController
@RequestMapping("/permission_record")
public class PermissionRecordController {

    private final PermissionRecordService permissionRecordService;

    public PermissionRecordController(PermissionRecordService permissionRecordService) {
        this.permissionRecordService = permissionRecordService;
    }

    @PostMapping("/bind")
    public ResponseDTO<Object> bindPermissionRecord(@Valid @RequestBody BindPermissionRecordDTO bindPermissionRecordDTO) {
        return permissionRecordService.bindPermissionRecord(bindPermissionRecordDTO);
    }

    @PostMapping("/get")
    public ResponseDTO<QueryPermissionRecordVO> getPermissionRecord(@Valid @RequestBody QueryPermissionRecordDTO queryPermissionRecordDTO) {
        return permissionRecordService.getPermissionRecord(queryPermissionRecordDTO);
    }
}
