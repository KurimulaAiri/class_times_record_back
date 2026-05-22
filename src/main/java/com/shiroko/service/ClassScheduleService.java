package com.shiroko.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.dto.classschedule.QueryClassScheduleDTO;
import com.shiroko.repository.entity.ClassSchedule;
import com.shiroko.repository.vo.classschedule.QueryClassScheduleVO;

/**
 * Description: 班级排课映射接口
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/16 下午22:11
 */
public interface ClassScheduleService extends IService<ClassSchedule> {

    ResponseDTO<QueryClassScheduleVO> getByClassId(QueryClassScheduleDTO dto);

    ResponseDTO<QueryClassScheduleVO> getByInstitutionId(QueryClassScheduleDTO dto);
}
