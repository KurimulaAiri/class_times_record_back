package com.shiroko.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiroko.converter.ParentConverter;
import com.shiroko.converter.StudentConverter;
import com.shiroko.mapper.InstitutionMapper;
import com.shiroko.mapper.ParentMapper;
import com.shiroko.mapper.ParentStudentMapper;
import com.shiroko.mapper.StudentMapper;
import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.dto.parent.UpdateParentDTO;
import com.shiroko.repository.dto.student.InsertStudentDTO;
import com.shiroko.repository.dto.student.QueryStudentDTO;
import com.shiroko.repository.dto.student.StudentDTO;
import com.shiroko.repository.dto.student.UpdateStudentDTO;
import com.shiroko.repository.entity.Parent;
import com.shiroko.repository.entity.ParentStudent;
import com.shiroko.repository.entity.Student;
import com.shiroko.repository.vo.parent.ParentVO;
import com.shiroko.repository.vo.student.InsertStudentVO;
import com.shiroko.repository.vo.student.QueryStudentVO;
import com.shiroko.repository.vo.student.StudentVO;
import com.shiroko.repository.vo.student.UpdateStudentVO;
import com.shiroko.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Description: 学生数据库实体类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/4/23 上午14:32
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
@RequiredArgsConstructor
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

    private final StudentMapper studentMapper;
    private final InstitutionMapper institutionMapper;
    private final ParentStudentMapper parentStudentMapper;
    private final ParentMapper parentMapper;

    private final StudentConverter studentConverter;
    private final ParentConverter parentConverter;

    @Override
    public ResponseDTO<QueryStudentVO> getStudentByParentId(QueryStudentDTO queryStudentDTO) {

        IPage<StudentDTO> page = new Page<>(
                queryStudentDTO.getCurrentPage() == null ? 1 : queryStudentDTO.getCurrentPage(),
                queryStudentDTO.getPageSize() == null ? 10 : queryStudentDTO.getPageSize()
        );

        studentMapper.selectStudentByParentId(page, queryStudentDTO);

        List<StudentDTO> list = page.getRecords();

        List<StudentVO> voList = studentConverter.dtoListToVOList(list);

        for (StudentVO vo : voList) {
            vo.setInstitutions(institutionMapper.selectListByStudentId(vo.getId()));
        }

        return ResponseDTO.success(new QueryStudentVO(voList, page.getTotal()));
    }

    @Override
    public ResponseDTO<UpdateStudentVO> updateStudent(UpdateStudentDTO updateStudentDTO) {

        UpdateParentDTO primaryParent = updateStudentDTO.getPrimaryParent();
        updateParentAndParentStudent(primaryParent, updateStudentDTO.getId());
        UpdateParentDTO secondaryParent = updateStudentDTO.getSecondaryParent();
        updateParentAndParentStudent(secondaryParent, updateStudentDTO.getId());

        studentMapper.updateById(studentConverter.updateStudentDTOToPojo(updateStudentDTO));

        return ResponseDTO.success("更新成功", new UpdateStudentVO(updateStudentDTO.getId()));
    }

    @Override
    public ResponseDTO<QueryStudentVO> getStudentByTeacherId(QueryStudentDTO queryStudentDTO) {
        // 1. 分页查询学生
        IPage<StudentDTO> page = getPage(queryStudentDTO);
        queryStudentDTO.setSex(queryStudentDTO.getSex() == -1 ? null : queryStudentDTO.getSex());
        log.debug("根据教师id查询学生列表，教师id：{}，查询参数：{}", queryStudentDTO.getTeacherId(), queryStudentDTO);
        studentMapper.selectStudentByTeacherId(page, queryStudentDTO);
        return injectParentInfo(page);
    }

    @Override
    public ResponseDTO<QueryStudentVO> getStudentByInstitutionId(QueryStudentDTO queryStudentDTO) {
        // 1. 分页查询学生
        IPage<StudentDTO> page = getPage(queryStudentDTO);
        queryStudentDTO.setSex(queryStudentDTO.getSex() == -1 ? null : queryStudentDTO.getSex());
        log.debug("根据机构id查询学生列表，机构id：{}，查询参数：{}", queryStudentDTO.getInstitutionId(), queryStudentDTO);
        studentMapper.selectStudentByInstitutionId(page, queryStudentDTO);
        return injectParentInfo(page);
    }

    @Override
    public ResponseDTO<QueryStudentVO> getStudentByClassId(QueryStudentDTO queryStudentDTO) {

        IPage<StudentDTO> page = getPage(queryStudentDTO);
        studentMapper.selectStudentByClassId(page, queryStudentDTO);
        return injectParentInfo(page);
    }

    @Override
    public ResponseDTO<InsertStudentVO> insertStudent(InsertStudentDTO insertStudentDTO) {
        Long psIdPrimary;
        Long psIdSecondary = null;

        // 插入学生表
        Student student = studentConverter.insertStudentDTOToPojo(insertStudentDTO);
        studentMapper.insert(student);

        // 插入主要家长
        Parent primaryParent = parentConverter.insertDTOToPojo(insertStudentDTO.getPrimaryParent());
        primaryParent.setUserId(0L);
        parentMapper.insert(primaryParent);

        ParentStudent primaryParentStudent = new ParentStudent(null, primaryParent.getParentId(), insertStudentDTO.getPrimaryParent().getRelation(), student.getId(), true, null);
        // 插入主要家长-学生表
        parentStudentMapper.insert(primaryParentStudent);
        psIdPrimary = primaryParentStudent.getId();

        // 插入次要家长
        if (insertStudentDTO.getSecondaryParent() != null) {
            // 插入次要家长
            Parent secondaryParent = parentConverter.insertDTOToPojo(insertStudentDTO.getSecondaryParent());
            secondaryParent.setUserId(0L);
            parentMapper.insert(secondaryParent);

            ParentStudent secondaryParentStudent = new ParentStudent(null, secondaryParent.getParentId(), insertStudentDTO.getSecondaryParent().getRelation(), student.getId(), false, null);
            // 插入次要家长-学生表
            parentStudentMapper.insert(secondaryParentStudent);
            psIdSecondary = secondaryParentStudent.getId();
        }

        if (psIdPrimary != null && insertStudentDTO.getSecondaryParent() != null && psIdSecondary != null) {
            return ResponseDTO.success("插入学生成功(包含次要家长)", new InsertStudentVO(student.getId()));
        } else if (psIdPrimary != null) {
            return ResponseDTO.success("插入学生成功(仅包含主要家长)", new InsertStudentVO(student.getId()));
        }
        return ResponseDTO.fail("插入学生失败");
    }

    @Override
    public ResponseDTO<QueryStudentVO> getStudentByStudentId(QueryStudentDTO queryStudentDTO) {
        StudentDTO student = studentMapper.selectByStudentId(queryStudentDTO);
        return injectParentInfo(student);
    }

    @Override
    public ResponseDTO<QueryStudentVO> getStudentByCourseId(QueryStudentDTO queryStudentDTO) {
        // 1. 分页查询学生
        IPage<StudentDTO> page = getPage(queryStudentDTO);
        studentMapper.selectStudentByCourseId(page, queryStudentDTO);
        return injectParentInfo(page);
    }

    @Override
    public ResponseDTO<QueryStudentVO> getStudent(QueryStudentDTO queryStudentDTO) {
        return null;
    }

    private ResponseDTO<QueryStudentVO> injectParentInfo(StudentDTO student) {
        if (student == null) {
            return ResponseDTO.fail("学生不存在");
        }
        // 1. 转换基础信息 POJO -> VO
        StudentVO vo = studentConverter.dtoToVO(student);

        // 2. 调用 Mapper 查询该学生关联的所有家长（复用批量查询接口，传入单个 ID 即可）
        List<ParentVO> parents = parentStudentMapper.selectAllBatchParents(List.of(student.getId()));

        // 3. 遍历家长列表，根据 isPrimary 标识位归类
        if (parents != null && !parents.isEmpty()) {
            for (ParentVO p : parents) {
                // 注意：这里建议使用 .equals(p.getIsPrimary()) 避免空指针
                if (Integer.valueOf(1).equals(p.getIsPrimary())) {
                    vo.setPrimaryParent(p);
                } else if (Integer.valueOf(0).equals(p.getIsPrimary())) {
                    vo.setSecondaryParent(p);
                }
            }
        }

        return ResponseDTO.success(new QueryStudentVO(Collections.singletonList(vo), 1L));
    }

    private ResponseDTO<QueryStudentVO> injectParentInfo(IPage<StudentDTO> page) {
        List<StudentDTO> students = page.getRecords();
        if (students.isEmpty()) {
            return ResponseDTO.success(new QueryStudentVO(new ArrayList<>(), 0L));
        }

        // 1. 获取本页所有学生 ID
        List<Long> studentIds = students.stream().map(StudentDTO::getId).toList();

        // 2. 一次性查出所有相关的家长记录（包含主次）
        List<ParentVO> allParents = parentStudentMapper.selectAllBatchParents(studentIds);

        // 3. 将家长按学生 ID 分组：Map<学生ID, 家长列表>
        Map<Long, List<ParentVO>> parentGroupMap = allParents.stream()
                .collect(Collectors.groupingBy(ParentVO::getStudentId));

        // 4. 组装 VO 列表
        List<StudentVO> voList = students.stream().map(student -> {
            StudentVO vo = studentConverter.dtoToVO(student);

            // 获取该学生关联的所有家长
            List<ParentVO> associatedParents = parentGroupMap.getOrDefault(student.getId(), Collections.emptyList());

            // 5. 遍历列表，根据 isPrimary 标识位分发到不同字段
            for (ParentVO p : associatedParents) {
                if (Integer.valueOf(1).equals(p.getIsPrimary())) {
                    vo.setPrimaryParent(p);
                } else if (Integer.valueOf(0).equals(p.getIsPrimary())) {
                    vo.setSecondaryParent(p);
                }
            }
            return vo;
        }).toList();

        return ResponseDTO.success(new QueryStudentVO(voList, page.getTotal()));
    }

    // 将返回值从 IPage 显式改为 Page 具体的实现类
    private Page<StudentDTO> getPage(QueryStudentDTO queryStudentDTO) {
        return new Page<>(
                queryStudentDTO.getCurrentPage() == null ? 1 : queryStudentDTO.getCurrentPage(),
                queryStudentDTO.getPageSize() == null ? 10 : queryStudentDTO.getPageSize()
        );
    }

    private void updateParentAndParentStudent(UpdateParentDTO parent, Long studentId) {
        if (parent != null) {
            // 更新家长
            Parent parentEntity = parentConverter.updateDTOToPojo(parent);

            // 处理 parentId 为 0 的情况
            // 没有家长 id ，说明是新增家长，设置 userId 为 0
            if (parentEntity.getParentId() == 0L) {
                parentEntity.setUserId(0L);
            }
            parentMapper.insertOrUpdate(parentEntity);

            // 更新家长-学生表
            ParentStudent parentStudent = parentStudentMapper.selectOne(new LambdaQueryWrapper<ParentStudent>()
                    .eq(ParentStudent::getParentId, parentEntity.getParentId())
                    .eq(ParentStudent::getStudentId, studentId)
                    .eq(ParentStudent::getIsPrimary, parent.getIsPrimary()));

            if (parentStudent != null) {
                parentStudent.setIsPrimary(parent.getIsPrimary());
                parentStudent.setRelation(parent.getRelation());
                parentStudentMapper.updateById(parentStudent);
            } else {
                // 新增家长-学生表记录
                parentStudentMapper.insert(
                        new ParentStudent(
                                null,
                                parentEntity.getParentId(),
                                parent.getRelation(),
                                studentId,
                                parent.getIsPrimary(),
                                null
                        )
                );
            }
        }
    }
}




