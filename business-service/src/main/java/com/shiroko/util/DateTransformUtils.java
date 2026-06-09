package com.shiroko.util;

import com.shiroko.annotation.BaseDateTimeToString;
import com.shiroko.annotation.BaseDateToString;
import com.shiroko.annotation.BaseTimeToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Description: 日期转换工具类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/6/6 上午12:37
 */
public class DateTransformUtils {
    // 自定义日期转换方法(LocalDateTime)
    @BaseDateTimeToString
    public static String dateTimeToString(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "暂无记录";
        }
        // 1. 定义格式化器
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 2. 直接对 LocalDateTime 进行格式化
        return dateTime.format(formatter);
    }

    // 自定义时间转换方法(LocalTime)
    @BaseTimeToString
    public static String timeToString(LocalTime time) {
        if (time == null) {
            return "暂无记录";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        // 2. 直接对 LocalTime 进行格式化
        return time.format(formatter);
    }

    // 自定义日期转换方法(LocalDate)
    @BaseDateToString
    public static String dateToString(LocalDate date) {
        if (date == null) {
            return "暂无记录";
        }
        // 1. 定义格式化器
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // 2. 直接对 LocalDate 进行格式化
        return date.format(formatter);
    }
}
