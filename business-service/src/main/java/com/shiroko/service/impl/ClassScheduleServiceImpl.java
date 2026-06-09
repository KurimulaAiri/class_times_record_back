package com.shiroko.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiroko.converter.ClassScheduleConverter;
import com.shiroko.mapper.ClassScheduleMapper;
import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.dto.classschedule.ClassScheduleDTO;
import com.shiroko.repository.dto.classschedule.QueryClassScheduleDTO;
import com.shiroko.repository.dto.classschedule.UpdateClassScheduleDTO;
import com.shiroko.repository.entity.ClassSchedule;
import com.shiroko.repository.vo.classschedule.ClassScheduleVO;
import com.shiroko.repository.vo.classschedule.QueryClassScheduleVO;
import com.shiroko.repository.vo.classschedule.UpdateClassScheduleVO;
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

    @Override
    public ResponseDTO<QueryClassScheduleVO> getByInstitutionId(QueryClassScheduleDTO dto) {
        IPage<ClassScheduleDTO> page = new Page<>(dto.getCurrentPage(), dto.getPageSize());

        page = classScheduleMapper.selectClassScheduleByInstitutionId(page, dto);

        List<ClassScheduleVO> voList = classScheduleConverter.dtoListToVOList(page.getRecords());
        return ResponseDTO.success(new QueryClassScheduleVO(voList, page.getTotal()));
    }

    @Override
    public ResponseDTO<UpdateClassScheduleVO> updateByClassScheduleId(UpdateClassScheduleDTO dto) {
        ClassSchedule classSchedule = classScheduleConverter.updateDtoToPojo(dto);
        classScheduleMapper.updateById(classSchedule);
        return ResponseDTO.success(new UpdateClassScheduleVO(List.of(classScheduleConverter.pojoToVO(classSchedule))));
    }

    @Override
    public ResponseDTO<QueryClassScheduleVO> getClassScheduleById(QueryClassScheduleDTO dto) {
        ClassSchedule classSchedule = classScheduleMapper.selectById(dto.getId());
        if (classSchedule == null) {
            return ResponseDTO.fail("排课记录不存在");
        }
        return ResponseDTO.success(new QueryClassScheduleVO(List.of(classScheduleConverter.pojoToVO(classSchedule)), 1L));
    }
}




