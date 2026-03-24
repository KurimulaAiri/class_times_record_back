package com.shiroko.converter;

import com.shiroko.repository.entity.PermissionRecord;
import com.shiroko.repository.vo.PermissionRecordVO;
import org.mapstruct.Mapper;

/**
 * Description: 权限记录转换器
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/3/24 下午3:03
 */
@Mapper(componentModel = "spring")
public interface PermissionRecordConverter extends BaseConverter<PermissionRecord, PermissionRecordVO> {
}
