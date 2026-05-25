package com.shiroko.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiroko.annotation.UpdateStudentCount;
import com.shiroko.converter.ClassConverter;
import com.shiroko.mapper.ClassMapper;
import com.shiroko.mapper.ClassScheduleMapper;
import com.shiroko.mapper.ClassStudentMapper;
import com.shiroko.mapper.ClassTeacherMapper;
import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.dto.clazz.ClassDTO;
import com.shiroko.repository.dto.clazz.InsertClassDTO;
import com.shiroko.repository.dto.clazz.QueryClassDTO;
import com.shiroko.repository.dto.clazz.UpdateClassDTO;
import com.shiroko.repository.entity.Class;
import com.shiroko.repository.entity.ClassSchedule;
import com.shiroko.repository.entity.ClassTeacher;
import com.shiroko.repository.entity.Teacher;
import com.shiroko.repository.vo.clazz.ClassVO;
import com.shiroko.repository.vo.clazz.InsertClassVO;
import com.shiroko.repository.vo.clazz.QueryClassVO;
import com.shiroko.repository.vo.clazz.UpdateClassVO;
import com.shiroko.service.ClassService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Description: 班级服务实现
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/7 下午20:27
 */
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
@Slf4j
public class ClassServiceImpl extends ServiceImpl<ClassMapper, Class> implements ClassService{

    private final ClassMapper classMapper;
    private final ClassConverter classConverter;
    private final ClassStudentMapper classStudentMapper;
    private final ClassTeacherMapper classTeacherMapper;
    private final ClassScheduleMapper classScheduleMapper;

    @Override
    public ResponseDTO<QueryClassVO> getClassesByStudentId(QueryClassDTO queryClassDTO) {

        List<ClassDTO> classList = classMapper.getClassesByStudentId(queryClassDTO);

        List<ClassVO> classVOList = classConverter.dtoListToVOList(classList);

        QueryClassVO queryClassVO = new QueryClassVO();
        queryClassVO.setClassList(classVOList);
        queryClassVO.setTotal((long) classList.size());

        return ResponseDTO.success(queryClassVO);
    }

    @Override
    public ResponseDTO<QueryClassVO> getClassesByTeacherId(QueryClassDTO queryClassDTO) {
        List<ClassDTO> classList = classMapper.getClassesByTeacherId(queryClassDTO);

        // System.out.println("classList: " + classList);

        List<ClassVO> classVOList = classConverter.dtoListToVOList(classList);

        // System.out.println("classVOList: " + classVOList);

        QueryClassVO queryClassVO = new QueryClassVO();
        queryClassVO.setClassList(classVOList);
        queryClassVO.setTotal((long) classList.size());

        return ResponseDTO.success(queryClassVO);
    }

    @Override
    public ResponseDTO<QueryClassVO> getClassesByInstitutionId(QueryClassDTO queryClassDTO) {
        List<ClassDTO> classList = classMapper.getClassesByInstitutionId(queryClassDTO);

        List<ClassVO> classVOList = classConverter.dtoListToVOList(classList);

        QueryClassVO queryClassVO = new QueryClassVO();
        queryClassVO.setClassList(classVOList);
        queryClassVO.setTotal((long) classList.size());

        return ResponseDTO.success(queryClassVO);
    }

    @Override
    @UpdateStudentCount
    public ResponseDTO<UpdateClassVO> addStudentToClass(UpdateClassDTO updateClassDTO) {
        Long result;
        try {
            result = classStudentMapper.insertBatch(updateClassDTO);
        } catch (DuplicateKeyException e) {
            return ResponseDTO.fail("班级中已存在该学生");
        }
        return ResponseDTO.success(new UpdateClassVO(result));
    }

    @Override
    public ResponseDTO<QueryClassVO> getClassById(QueryClassDTO queryClassDTO) {
        ClassDTO clazz = classMapper.selectByClassId(queryClassDTO);
        if (clazz == null) {
            return ResponseDTO.fail("班级不存在");
        }
        return ResponseDTO.success(new QueryClassVO(List.of(classConverter.dtoToVo(clazz)), 1L));
    }

    @Override
    public ResponseDTO<InsertClassVO> insertClass(InsertClassDTO insertClassDTO) {
        // 1. 插入班级基础信息
        Class newClazz = new Class();
        newClazz.setClassName(insertClassDTO.getClassName());
        newClazz.setCourseId(insertClassDTO.getCourseId());
        newClazz.setStudentMaxCount(insertClassDTO.getMaxCount());

        // 假设你使用的是 MyBatis-Plus 或配置了主键回填，执行完 insert 后 newClazz.getId() 就能拿到自增 ID
        classMapper.insert(newClazz);
        Long classId = newClazz.getId();

        // 2. 批量插入班级与老师的关联表 (class_teacher)
        List<Teacher> teachers = insertClassDTO.getTeachers();
        if (teachers != null && !teachers.isEmpty()) {
            // 构造实体集合（也可以直接在 Mapper 用 @Param 传 list，看你的习惯，这里用实体集合演示）
            List<ClassTeacher> classTeachers = teachers.stream().map(teacher -> {
                ClassTeacher ct = new ClassTeacher();
                ct.setClassId(classId);
                ct.setTeacherId(teacher.getTeacherId());
                return ct;
            }).toList();
            // 调用批量插入接口
            classTeacherMapper.insertBatch(classTeachers);
        }

        // 3. 批量插入班级排班日程表 (class_schedule)
        List<ClassSchedule> schedules = insertClassDTO.getSchedules();
        if (schedules != null && !schedules.isEmpty()) {
            List<ClassSchedule> classSchedules = schedules.stream().map(dto -> {
                ClassSchedule cs = new ClassSchedule();
                cs.setClassId(classId);
                cs.setDayOfWeek(dto.getDayOfWeek()); // 你刚改好的单选数字
                cs.setStartDate(dto.getStartDate());
                cs.setEndDate(dto.getEndDate());
                cs.setStartTime(dto.getStartTime());
                cs.setEndTime(dto.getEndTime());
                cs.setRemark(dto.getRemark());
                return cs;
            }).collect(Collectors.toList());

            // 调用批量插入接口
            classScheduleMapper.insertBatch(classSchedules);
        }

        // 4. 返回前端需要的出参 VO
        InsertClassVO vo = new InsertClassVO();
        vo.setClassId(classId);
        vo.setClassName(newClazz.getClassName());

        return ResponseDTO.success(vo);
    }

    @Override
    @UpdateStudentCount
    public ResponseDTO<UpdateClassVO> removeStudentFromClass(UpdateClassDTO updateClassDTO) {
        Long result = classStudentMapper.deleteBatch(updateClassDTO);
        return ResponseDTO.success(new UpdateClassVO(result));
    }

    @Override
    @Transactional(rollbackFor = Exception.class) // 💡 涉及多表操作，强烈建议加上事务注解
    public ResponseDTO<UpdateClassVO> updateClassByClassId(UpdateClassDTO updateClassDTO) {
        Long insertSResult = 0L;
        Long insertCTResult = 0L;

        // 1. 转换并更新班级基本信息
        Class clazz = classConverter.updateDtoToPojo(updateClassDTO);

        // 针对 MyBatis-Plus 的优化：updateById 默认会自动忽略 null 字段，只更新传过来的字段
        int result = classMapper.updateById(clazz);
        if (result == 0) {
            return ResponseDTO.fail("更新失败");
        }

        // 2. 核心改动：如果前端指定了“只更新班级自身字段”，则直接返回，跳过老师和日程的“先删后增”
        if (Boolean.TRUE.equals(updateClassDTO.getOnlyUpdateClassOwn())) {
            return ResponseDTO.success("更新班级信息成功", new UpdateClassVO(clazz.getId()));
        }

        // ================= 以下是原有的老师和日程处理逻辑 =================
        // 3. 处理【任课老师】的“有加有减” -> 先删后增
        classTeacherMapper.deleteByClassId(clazz.getId());
        log.debug("删除班级{}的所有老师关联记录", clazz.getId());
        if (updateClassDTO.getTeachers() != null && !updateClassDTO.getTeachers().isEmpty()) {
            log.debug("插入班级{}的新老师关联记录", clazz.getId());
            List<ClassTeacher> classTeachers = updateClassDTO.getTeachers().stream().map(teacher -> {
                ClassTeacher ct = new ClassTeacher();
                ct.setClassId(updateClassDTO.getClassId());
                ct.setTeacherId(teacher.getTeacherId());
                return ct;
            }).collect(Collectors.toList());
            insertCTResult = classTeacherMapper.insertBatch(classTeachers);
        }

        // 4. 处理【上课日程】的“有加有减” -> 先删后增
        classScheduleMapper.deleteByClassId(clazz.getId());
        log.debug("删除班级{}的所有日程记录", clazz.getId());
        if (updateClassDTO.getSchedules() != null && !updateClassDTO.getSchedules().isEmpty()) {
            log.debug("插入班级{}的新日程记录", clazz.getId());
            insertSResult = classScheduleMapper.insertBatch(updateClassDTO.getSchedules());
        }

        return ResponseDTO.success("更新成功，更新了" + insertCTResult + "条老师关联记录，更新了" + insertSResult + "条日程记录", new UpdateClassVO(clazz.getId()));
    }


}




