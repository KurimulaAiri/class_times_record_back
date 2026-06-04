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

    private final CourseRecordConverter courseRecordConverter;

    // Service 逻辑简化
    public ResponseDTO<QueryCourseRecordVO> getCourseRecords(QueryCourseRecordDTO dto) {
        // 1. 获取当前用户 ID
        dto.setUserId(UserContext.getUser().getId());

        // 2. 构造分页对象 (如果没有传分页参数，可以给个默认值或者不分页)
        // 注意：即便不分页，也可以传一个 Page(1, -1) 或者通过条件判断
        IPage<CourseRecordVO> pageParam = new Page<>(
                dto.getCurrentPage() == null ? 1 : dto.getCurrentPage(),
                dto.getPageSize() == null ? 10 : dto.getPageSize()
        );

        // 3. 执行查询 (MyBatis Plus 会自动把结果填充回 pageParam)
        courseRecordMapper.selectCourseCustomPage(pageParam, dto);

        // 4. 处理 VO 注入逻辑
        List<CourseRecordVO> list = pageParam.getRecords();

        injectPermissionType(list);

        // 5. 返回封装结果
        return ResponseDTO.success(new QueryCourseRecordVO(list, pageParam.getTotal()));
    }

    @Override
    public ResponseDTO<Object> addCourseRecord(InsertCourseRecordDTO dto) {
        CourseRecord courseRecord = courseRecordConverter.insertDtoToPojo(dto);
        courseRecord.setCourseOwnerUserId(UserContext.getUser().getId());
        int rowsInserted = courseRecordMapper.insert(
                courseRecord
        );
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
        System.out.println(courseRecord);
        int rowsUpdated = courseRecordMapper.updateById(
                courseRecord
        );
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
        // Map<courseId, totalDeductCount>
        Map<Long, Integer> combinedMap = dto.getClasses().stream()
                .collect(Collectors.groupingBy(
                        DeductClassDTO::getCourseId,
                        Collectors.summingInt(DeductClassDTO::getDeductCount)
                ));

        // 2. 批量更新数据库
        combinedMap.forEach((courseId, totalCount) -> {
            // SQL: UPDATE course_record SET course_rest_time = course_rest_time - #{totalCount}
            // WHERE id = #{courseId} AND student_id = #{studentId}
            CourseRecord cr = new CourseRecord()
                    .setCourseId(courseId)
                    .setStudentId(dto.getStudentId());
            Integer rows = courseRecordMapper.updateRestTime(cr, totalCount);
            res.updateAndGet(v -> v + rows);
            if (rows == 0) {
                Course course = courseMapper.selectById(courseId);
                throw new BusinessException(ResultCode.COURSE_BALANCE_NOT_ENOUGH, "课程 [ " + course.getCourseName() + " ] 余额不足");
            } else {
                // 课程余额充足，更新成功，添加记录
                recordMapper.insert(new Record()
                        .setCourseRecordId(cr.getId())
                        .setRecordTime(dto.getRecordTime())
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

        // 因为学生不会重复，直接遍历前端传来的学生列表即可
        dto.getStudents().forEach(studentDto -> {
            Long studentId = studentDto.getStudentId();
            Integer deductCount = studentDto.getDeductCount();

            // 1. 构建更新条件
            // WHERE course_id = #{courseId} AND student_id = #{studentId}
            CourseRecord cr = new CourseRecord()
                    .setCourseId(dto.getCourseId())
                    .setStudentId(studentId);

            // 2. 扣减课时
            Integer rows = courseRecordMapper.updateRestTime(cr, deductCount);
            res.updateAndGet(v -> v + rows);

            // 3. 校验扣减结果
            if (rows == 0) {
                // 扣除失败，抛出异常阻断事务，并提示哪个学生课时不够
                Student student = studentMapper.selectById(studentId);
                String studentName = student != null ? student.getStudentName() : "未知学员";
                throw new BusinessException(ResultCode.COURSE_BALANCE_NOT_ENOUGH, "学员 [ " + studentName + " ] 课时余额不足");
            } else {
                // 4. 扣除成功，记录扣课流水
                recordMapper.insert(new Record()
                        .setCourseRecordId(cr.getId())
                        .setRecordTime(dto.getRecordTime())
                        .setRecordRemark(dto.getRemark())
                        .setRecordType(2L) // 2 为减少
                        .setRecordChange(Long.valueOf(deductCount))
                );
            }
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
     * Description: 注入权限类型
     *
     * @param voList 课程记录 VO 列表
     */
    private void injectPermissionType(List<CourseRecordVO> voList) {
        // 2. 批量处理关联字段 (核心增强)
        if (!voList.isEmpty()) {
            // 2.1 提取当前页所有的 courseId
            List<Long> courseIds = voList.stream()
                    .map(CourseRecordVO::getId)
                    .toList();

            // 2.2 调用你写好的 Service 批量查询权限记录
            // 假设你要查当前用户的权限类型，WHERE course_id IN (...) AND user_id = current
            List<PermissionRecord> permissions = permissionRecordMapper.selectList(
                    new LambdaQueryWrapper<PermissionRecord>()
                            .in(PermissionRecord::getCourseRecordId, courseIds)
                            .eq(PermissionRecord::getUserId, UserContext.getUser().getId())
            );

            // 2.3 将查询结果转为 Map 结构 (Key: courseId, Value: 权限字段)
            Map<Long, Long> permissionMap = permissions.stream()
                    .collect(Collectors.toMap(
                            PermissionRecord::getCourseRecordId,
                            PermissionRecord::getPermissionType, // 假设你要拿这个字段
                            (existing, replacement) -> existing // 防止重复 Key 冲突
                    ));

            // 2.4 回填到 voList 中
            voList.forEach(vo -> {
                Long type = permissionMap.get(vo.getId());
                vo.setPermissionType(type);
            });
        }
    }
}
