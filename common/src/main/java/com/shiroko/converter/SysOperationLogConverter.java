package com.shiroko.converter;

import com.shiroko.annotation.BaseDateTimeToString;
import com.shiroko.repository.entity.SysOperationLog;
import com.shiroko.repository.vo.admin.SysOperationLogVO;
import com.shiroko.util.DateTransformUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {DateTransformUtils.class})
public interface SysOperationLogConverter extends BaseConverter<SysOperationLog, SysOperationLogVO> {

    @Mapping(target = "createTimeStr", source = "createTime", qualifiedBy = BaseDateTimeToString.class)
    @Override
    SysOperationLogVO pojoToVO(SysOperationLog pojo);
}
