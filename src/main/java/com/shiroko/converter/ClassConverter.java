package com.shiroko.converter;

import com.shiroko.annotation.BaseDateTimeToString;
import com.shiroko.repository.dto.clazz.ClassDTO;
import com.shiroko.repository.dto.clazz.UpdateClassDTO;
import com.shiroko.repository.entity.Class;
import com.shiroko.repository.vo.clazz.ClassVO;
import com.shiroko.util.DateTransformUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Description: 班级转换器
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/7 下午9:18
 */
@Mapper(componentModel = "spring", uses = {DateTransformUtils.class})
public interface ClassConverter extends BaseConverter<Class, ClassVO> {

    @Mapping(target = "createTimeStr", source = "createTime", qualifiedBy = BaseDateTimeToString.class)
    @Mapping(target = "updateTimeStr", source = "updateTime", qualifiedBy = BaseDateTimeToString.class)
    ClassVO dtoToVo(ClassDTO dto);

    List<ClassVO> dtoListToVOList(List<ClassDTO> dtoList);

    @Mapping(target = "id", source = "classId")
    Class updateDtoToPojo(UpdateClassDTO updateClassDTO);
}
