package com.shiroko.repository.vo.classschedule;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Description: 班级排课查询VO类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/16 下午10:32
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryClassScheduleVO {

    private List<ClassScheduleVO> classSchedules;

    private Long total;

}
