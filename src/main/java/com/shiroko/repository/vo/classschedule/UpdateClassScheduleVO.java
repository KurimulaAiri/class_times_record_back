package com.shiroko.repository.vo.classschedule;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Description: 更新班级排课VO
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/31 下午6:43
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateClassScheduleVO {

    List<ClassScheduleVO> classSchedules;

}
