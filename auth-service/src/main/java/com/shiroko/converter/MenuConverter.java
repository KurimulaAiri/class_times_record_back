package com.shiroko.converter;

import com.shiroko.annotation.BaseDateTimeToString;
import com.shiroko.repository.entity.Menu;
import com.shiroko.repository.vo.menu.MenuVO;
import com.shiroko.util.DateTransformUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Description: 菜单转换器
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/4/18 下午3:34
 */
@Mapper(componentModel = "spring", uses = {DateTransformUtils.class})
public interface MenuConverter extends BaseConverter<Menu, MenuVO> {

    @Mapping(source = "createTime", target = "createTimeStr", qualifiedBy = BaseDateTimeToString.class) // 自定义转换规则
    @Mapping(source = "updateTime", target = "updateTimeStr", qualifiedBy = BaseDateTimeToString.class) // 自定义转换规则
    @Override
    MenuVO pojoToVO(Menu pojo);

}
