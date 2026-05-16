package com.shiroko.converter;

import org.mapstruct.Named;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    // 自定义日期转换方法(LocalDateTime)
    @Named("dateTimeToString")
    default String dateTimeToString(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "暂无记录";
        }
        // 1. 定义格式化器
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 2. 直接对 LocalDateTime 进行格式化
        return dateTime.format(formatter);
    }

    // 自定义时间转换方法(LocalTime)
    @Named("timeToString")
    default String timeToString(LocalTime time) {
        if (time == null) {
            return "暂无记录";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        // 2. 直接对 LocalTime 进行格式化
        return time.format(formatter);
    }

    // 自定义日期转换方法(LocalDate)
    @Named("dateToString")
    default String dateToString(LocalDate date) {
        if (date == null) {
            return "暂无记录";
        }
        // 1. 定义格式化器
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // 2. 直接对 LocalDate 进行格式化
        return date.format(formatter);
    }
}
