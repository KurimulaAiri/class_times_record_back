package com.shiroko.converter;

import java.util.List;

/**
 * Description: 基础转换器接口
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/3/19 下午11:40
 */
public interface BaseConverter<T, VO> {

    VO pojoToVO(T pojo);

    // 集合对象转换（MapStruct会自动生成实现）
    List<VO> pojoListToVOList(List<T> pojoList);
}
