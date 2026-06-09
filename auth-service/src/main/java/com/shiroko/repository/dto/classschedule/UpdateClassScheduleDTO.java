package com.shiroko.repository.dto.classschedule;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Description: 更新班级时间DTO
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/31 下午6:42
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateClassScheduleDTO {

    private Long id;

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalTime startTime;

    private LocalTime endTime;

    private String remark;

    private Long dayOfWeek;

}
