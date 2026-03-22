package com.shiroko.converter;

import org.mapstruct.Named;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    default String dateToString(LocalDateTime date) {
        if (date == null) {
            return "暂无记录";
        }
        // 1. 定义格式化器
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 2. 直接对 LocalDateTime 进行格式化
        return date.format(formatter);
    }
}
