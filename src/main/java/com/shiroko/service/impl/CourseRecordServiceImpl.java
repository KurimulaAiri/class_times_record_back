package com.shiroko.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shiroko.context.UserContext;
import com.shiroko.converter.CourseRecordConverter;
import com.shiroko.mapper.CourseRecordMapper;
import com.shiroko.mapper.PermissionRecordMapper;
import com.shiroko.repository.dto.*;
import com.shiroko.repository.entity.CourseRecord;
import com.shiroko.repository.entity.PermissionRecord;
import com.shiroko.repository.vo.CourseRecordVO;
import com.shiroko.repository.vo.QueryCourseRecordVO;
import com.shiroko.service.CourseRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
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
public class CourseRecordServiceImpl implements CourseRecordService {

    private final CourseRecordMapper courseRecordMapper;

    private final CourseRecordConverter courseRecordConverter;

    private final PermissionRecordMapper permissionRecordMapper;

    // Service 逻辑简化
    public ResponseDTO<QueryCourseRecordVO> getCourseRecords(QueryCourseRecordDTO dto) {
        // 1. 获取当前用户 ID
        dto.setUserId(UserContext.getUser().getId());

        // 2. 构造分页对象 (如果没有传分页参数，可以给个默认值或者不分页)
        // 注意：即便不分页，也可以传一个 Page(1, -1) 或者通过条件判断
        IPage<CourseRecord> pageParam = new Page<>(
                dto.getCurrentPage() == null ? 1 : dto.getCurrentPage(),
                dto.getPageSize() == null ? 10 : dto.getPageSize()
        );

        // 3. 执行查询 (MyBatis Plus 会自动把结果填充回 pageParam)
        courseRecordMapper.selectCourseCustomPage(pageParam, dto);

        // 4. 处理 VO 注入逻辑
        List<CourseRecord> list = pageParam.getRecords();

        List<CourseRecordVO> voList = courseRecordConverter.pojoListToVOList(list);

        injectPermissionType(voList);

        // 5. 返回封装结果
        return ResponseDTO.success(new QueryCourseRecordVO(voList, pageParam.getTotal()));
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
        return ResponseDTO.success("更新成功，影响记录数：" + rowsUpdated);
    }

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
