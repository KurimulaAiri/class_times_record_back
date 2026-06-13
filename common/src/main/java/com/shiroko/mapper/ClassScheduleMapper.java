package com.shiroko.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shiroko.repository.dto.classschedule.ClassScheduleDTO;
import com.shiroko.repository.dto.classschedule.QueryClassScheduleDTO;
import com.shiroko.repository.entity.ClassSchedule;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Description: 班级排班日程表Mapper接口类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/18 上午10:29
 */
public interface ClassScheduleMapper extends BaseMapper<ClassSchedule> {

    Long insertBatch(List<ClassSchedule> classScheduleList);

    Long deleteByClassId(Long classId);

    IPage<ClassScheduleDTO> selectClassScheduleByInstitutionId(IPage<ClassScheduleDTO> page, @Param("dto") QueryClassScheduleDTO dto);
}
