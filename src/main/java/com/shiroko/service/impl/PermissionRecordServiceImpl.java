package com.shiroko.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiroko.context.UserContext;
import com.shiroko.repository.dto.BindPermissionRecordDTO;
import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.entity.PermissionRecord;
import com.shiroko.service.PermissionRecordService;
import com.shiroko.mapper.PermissionRecordMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Description: 管理员分组记录Service实现类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/3/21 下午14:35
 */
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class PermissionRecordServiceImpl extends ServiceImpl<PermissionRecordMapper, PermissionRecord> implements PermissionRecordService {

    private final PermissionRecordMapper permissionRecordMapper;

    @Override
    public ResponseDTO<Object> bindPermissionRecord(BindPermissionRecordDTO bindPermissionRecordDTO) {
        Long currentUserId = UserContext.getUser().getId();

        PermissionRecord newRecord = new PermissionRecord()
                .setUserId(currentUserId)
                .setCourseRecordId(bindPermissionRecordDTO.getCourseRecordId())
                .setPermissionType(bindPermissionRecordDTO.getPermissionType());
        int rowsAffected = permissionRecordMapper.upsertPermission(newRecord);
        return ResponseDTO.success("绑定成功，影响行数：" + rowsAffected);
    }
}




