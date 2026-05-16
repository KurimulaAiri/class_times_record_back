package com.shiroko.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiroko.converter.ClassScheduleConverter;
import com.shiroko.mapper.ClassScheduleMapper;
import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.dto.classschedule.QueryClassScheduleDTO;
import com.shiroko.repository.entity.ClassSchedule;
import com.shiroko.repository.vo.classschedule.ClassScheduleVO;
import com.shiroko.repository.vo.classschedule.QueryClassScheduleVO;
import com.shiroko.service.ClassScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description: 班级排课映射接口
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/16 下午22:11
 */
@Service
@RequiredArgsConstructor
public class ClassScheduleServiceImpl extends ServiceImpl<ClassScheduleMapper, ClassSchedule> implements ClassScheduleService {

    private final ClassScheduleMapper classScheduleMapper;
    private final ClassScheduleConverter classScheduleConverter;

    @Override
    public ResponseDTO<QueryClassScheduleVO> getByClassId(QueryClassScheduleDTO dto) {
        IPage<ClassSchedule> page = new Page<>(dto.getCurrentPage(), dto.getPageSize());
        classScheduleMapper.selectPage(page, new LambdaQueryWrapper<ClassSchedule>()
                .eq(ClassSchedule::getClassId, dto.getClassId()));

        List<ClassScheduleVO> voList = classScheduleConverter.pojoListToVOList(page.getRecords());
        return ResponseDTO.success(new QueryClassScheduleVO(voList, page.getTotal()));

    }
}




