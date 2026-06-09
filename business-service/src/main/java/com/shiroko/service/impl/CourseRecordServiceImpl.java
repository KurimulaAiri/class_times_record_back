package com.shiroko.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiroko.common.enums.ResultCode;
import com.shiroko.context.UserContext;
import com.shiroko.converter.CourseRecordConverter;
import com.shiroko.exception.BusinessException;
import com.shiroko.mapper.*;
import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.dto.clazz.DeductClassDTO;
import com.shiroko.repository.dto.courserecord.*;
import com.shiroko.repository.entity.Class;
import com.shiroko.repository.entity.Record;
import com.shiroko.repository.entity.*;
import com.shiroko.repository.vo.courserecord.CourseRecordVO;
import com.shiroko.repository.vo.courserecord.DeductCourseRecordVO;
import com.shiroko.repository.vo.courserecord.QueryCourseRecordVO;
import com.shiroko.service.CourseRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * Description: 课程记录服务实现类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/2/7 上午1:24
 */
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
@Slf4j
public class CourseRecordServiceImpl extends ServiceImpl<CourseRecordMapper, CourseRecord> implements CourseRecordService {

    private final PermissionRecordMapper permissionRecordMapper;
    private final CourseMapper courseMapper;
    private final RecordMapper recordMapper;
    private final CourseRecordMapper courseRecordMapper;
    private final StudentMapper studentMapper;
    private final ClassMapper classMapper;
    private final ClassStudentMapper classStudentMapper;

    private final CourseRecordConverter courseRecordConverter;

    @Override
    public ResponseDTO<QueryCourseRecordVO> getCourseRecords(QueryCourseRecordDTO dto) {
        dto.setUserId(UserContext.getUser().getId());

        IPage<CourseRecordVO> pageParam = new Page<>(
                dto.getCurrentPage() == null ? 1 : dto.getCurrentPage(),
                dto.getPageSize() == null ? 10 : dto.getPageSize()
        );

        courseRecordMapper.selectCourseCustomPage(pageParam, dto);
        List<CourseRecordVO> list = pageParam.getRecords();

        injectPermissionType(list);

        return ResponseDTO.success(new QueryCourseRecordVO(list, pageParam.getTotal()));
    }

    @Override
    public ResponseDTO<Object> addCourseRecord(InsertCourseRecordDTO dto) {
        CourseRecord courseRecord = courseRecordConverter.insertDtoToPojo(dto);
        courseRecord.setCourseOwnerUserId(UserContext.getUser().getId());
        int rowsInserted = courseRecordMapper.insert(courseRecord);

        PermissionRecord permissionRecord = new PermissionRecord()
                .setCourseRecordId(courseRecord.getId())
                .setUserId(UserContext.getUser().getId())
                .setPermissionType(1L);
        int rowsInsertedAdminGroupRecord = permissionRecordMapper.insert(permissionRecord);

        return ResponseDTO.success("添加成功，影响记录数：" + rowsInserted + "\n管理员分组记录数：" + rowsInsertedAdminGroupRecord, new int[]{rowsInserted, rowsInsertedAdminGroupRecord});
    }

    @Override
    public ResponseDTO<Object> deleteCourseRecord(DeleteCourseRecordDTO dto) {
        int rowsDeleted = courseRecordMapper.updateById(
                new CourseRecord()
                        .setId(dto.getCourseRecordId())
                        .setIsDelete(true)
        );
        return ResponseDTO.success("删除成功，影响记录数：" + rowsDeleted);
    }

    @Override
    public ResponseDTO<Object> updateCourseRecord(UpdateCourseRecordDTO dto) {
        CourseRecord courseRecord = courseRecordConverter.updateDtoToPojo(dto);
        int rowsUpdated = courseRecordMapper.updateById(courseRecord);
        return ResponseDTO.success(rowsUpdated);
    }

    @Override
    public ResponseDTO<QueryCourseRecordVO> newGetCourseRecords(QueryCourseRecordDTO dto) {
        IPage<CourseRecordDTO> pageParam = new Page<>(
                dto.getCurrentPage() == null ? 1 : dto.getCurrentPage(),
                dto.getPageSize() == null ? 10 : dto.getPageSize()
        );
        courseRecordMapper.selectCourseRecords(pageParam, dto);
        List<CourseRecordDTO> list = pageParam.getRecords();

        List<CourseRecordVO> voList = courseRecordConverter.dtoListToVOList(list);
        injectPermissionType(voList);

        return ResponseDTO.success(new QueryCourseRecordVO(voList, pageParam.getTotal()));
    }

    @Override
    public ResponseDTO<DeductCourseRecordVO> deductByStudentId(DeductCourseRecordDTO dto) {
        AtomicReference<Integer> res = new AtomicReference<>(0);

        // 1. 数据聚合：将相同 courseId 的扣除数量相加
        Map<Long, Integer> combinedMap = dto.getClasses().stream()
                .collect(Collectors.groupingBy(
                        DeductClassDTO::getCourseId,
                        Collectors.summingInt(DeductClassDTO::getDeductCount)
                ));

        // 2. 批量更新数据库
        combinedMap.forEach((courseId, totalCount) -> {
            CourseRecord cr = new CourseRecord()
                    .setCourseId(courseId)
                    .setStudentId(dto.getStudentId());
            Integer rows = courseRecordMapper.updateRestTime(cr, totalCount);
            res.updateAndGet(v -> v + rows);

            if (rows == 0) {
                Course course = courseMapper.selectById(courseId);
                throw new BusinessException(ResultCode.COURSE_BALANCE_NOT_ENOUGH, "课程 [ " + course.getCourseName() + " ] 余额不足");
            } else {
                recordMapper.insert(new Record()
                        .setCourseRecordId(cr.getId())
                        .setRecordTime(dto.getRecordTime())
                        .setOperateTeacherId(dto.getOperatorId())
                        .setRecordRemark(dto.getRemark())
                        .setRecordType(2L) // 2 为减少
                        .setRecordChange(Long.valueOf(totalCount))
                );
            }
        });
        return ResponseDTO.success(new DeductCourseRecordVO(res.get()));
    }

    @Override
    public ResponseDTO<DeductCourseRecordVO> deductByCourseId(DeductCourseRecordDTO dto) {
        AtomicReference<Integer> res = new AtomicReference<>(0);

        // 提取复用：遍历前端传来的学生列表，逐个调用扣费公共方法
        dto.getStudents().forEach(studentDto -> {
            int rows = executeStudentDeduct(dto.getCourseId(), studentDto.getStudentId(), studentDto.getDeductCount(), dto);
            res.updateAndGet(v -> v + rows);
        });

        return ResponseDTO.success(new DeductCourseRecordVO(res.get()));
    }

    @Override
    public ResponseDTO<DeductCourseRecordVO> deductByClassId(DeductCourseRecordDTO dto) {
        // 1. 查询班级信息，获取 courseId
        Class clazz = classMapper.selectById(dto.getClassId());
        if (clazz == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "班级不存在");
        }
        Long courseId = clazz.getCourseId();

        // 2. 查询班级下所有学生
        List<ClassStudent> classStudents = classStudentMapper.selectList(
                new LambdaQueryWrapper<ClassStudent>()
                        .eq(ClassStudent::getClassId, dto.getClassId())
        );
        if (classStudents.isEmpty()) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "班级中没有学生");
        }

        AtomicReference<Integer> res = new AtomicReference<>(0);

        // 3. 提取复用：遍历班级学生，逐个调用扣费公共方法
        classStudents.forEach(cs -> {
            int rows = executeStudentDeduct(courseId, cs.getStudentId(), dto.getDeductCount(), dto);
            res.updateAndGet(v -> v + rows);
        });

        return ResponseDTO.success(new DeductCourseRecordVO(res.get()));
    }

    @Override
    public ResponseDTO<Object> insertCourseRecord(InsertCourseRecordDTO insertCourseRecordDTO) {
        CourseRecord newCourseRecord = courseRecordConverter.insertDtoToPojo(insertCourseRecordDTO);
        this.baseMapper.insert(newCourseRecord);
        return ResponseDTO.success(courseRecordConverter.pojoToVO(newCourseRecord));
    }

    /**
     * Core Extracted Method: 执行单个学员的扣费、校验与流水记录
     *
     * @param courseId    课程 ID
     * @param studentId   学生 ID
     * @param deductCount 扣减课时数
     * @param dto         基础上下文信息DTO (包含remark, recordTime等)
     * @return 影响行数
     */
    private int executeStudentDeduct(Long courseId, Long studentId, Integer deductCount, DeductCourseRecordDTO dto) {
        // 1. 构建更新条件条件
        CourseRecord cr = new CourseRecord()
                .setCourseId(courseId)
                .setStudentId(studentId);

        // 2. 扣减课时余额
        Integer rows = courseRecordMapper.updateRestTime(cr, deductCount);

        // 3. 余额不足校验
        if (rows == 0) {
            Student student = studentMapper.selectById(studentId);
            String studentName = student != null ? student.getStudentName() : "未知学员";
            throw new BusinessException(ResultCode.COURSE_BALANCE_NOT_ENOUGH, "学员 [ " + studentName + " ] 课时余额不足");
        }

        // 4. 扣除成功，记录流水明细
        recordMapper.insert(new Record()
                .setCourseRecordId(cr.getId())
                .setRecordTime(dto.getRecordTime())
                .setOperateTeacherId(dto.getOperatorId())
                .setRecordRemark(dto.getRemark())
                .setRecordType(2L) // 2 为减少
                .setRecordChange(Long.valueOf(deductCount))
        );

        return rows;
    }

    /**
     * Description: 注入权限类型
     *
     * @param voList 课程记录 VO 列表
     */
    private void injectPermissionType(List<CourseRecordVO> voList) {
        if (!voList.isEmpty()) {
            List<Long> courseIds = voList.stream()
                    .map(CourseRecordVO::getId)
                    .toList();

            List<PermissionRecord> permissions = permissionRecordMapper.selectList(
                    new LambdaQueryWrapper<PermissionRecord>()
                            .in(PermissionRecord::getCourseRecordId, courseIds)
                            .eq(PermissionRecord::getUserId, UserContext.getUser().getId())
            );

            Map<Long, Long> permissionMap = permissions.stream()
                    .collect(Collectors.toMap(
                            PermissionRecord::getCourseRecordId,
                            PermissionRecord::getPermissionType,
                            (existing, replacement) -> existing
                    ));

            voList.forEach(vo -> {
                Long type = permissionMap.get(vo.getId());
                vo.setPermissionType(type);
            });
        }
    }
}
