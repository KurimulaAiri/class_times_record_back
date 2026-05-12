package com.shiroko.converter;

import com.shiroko.repository.dto.parent.InsertParentDTO;
import com.shiroko.repository.dto.parent.UpdateParentDTO;
import com.shiroko.repository.entity.Parent;
import com.shiroko.repository.vo.parent.ParentVO;
import org.mapstruct.Mapper;

/**
 * Description: 父母类转换器
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/11 下午2:56
 */
@Mapper(componentModel = "spring")
public interface ParentConverter extends BaseConverter<Parent, ParentVO> {

    @Override
    ParentVO pojoToVO(Parent parent);

    Parent insertDTOToPojo(InsertParentDTO insertParentDTO);

    Parent updateDTOToPojo(UpdateParentDTO updateParentDTO);
}
