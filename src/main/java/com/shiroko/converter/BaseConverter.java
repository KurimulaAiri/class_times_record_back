package com.shiroko.converter;

import org.mapstruct.Named;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Description: 基础转换器接口
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/3/19 下午11:40
 */
public interface BaseConverter<T, V> {

    V pojoToVO(T pojo);

    // 集合对象转换（MapStruct会自动生成实现）
    List<V> pojoListToVOList(List<T> pojoList);

    // 自定义日期转换方法
    @Named("dateToString")
    default String dateToString(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }
}
