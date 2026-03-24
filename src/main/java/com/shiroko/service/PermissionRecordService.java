package com.shiroko.service;

import com.shiroko.repository.dto.BindPermissionRecordDTO;
import com.shiroko.repository.dto.QueryPermissionRecordDTO;
import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.entity.PermissionRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shiroko.repository.vo.QueryPermissionRecordVO;

/**
 * Description: 管理员分组记录Service接口
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/3/21 下午14:35
 */
public interface PermissionRecordService extends IService<PermissionRecord> {

    ResponseDTO<Object> bindPermissionRecord(BindPermissionRecordDTO bindPermissionRecordDTO);

    ResponseDTO<QueryPermissionRecordVO> getPermissionRecord(QueryPermissionRecordDTO queryPermissionRecordDTO);
}
