package com.shiroko.service.impl;

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
import com.shiroko.service.StudentService;
import lombok.RequiredArgsConstructor;
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
    public ResponseDTO<Void> updateStudent(UpdateStudentDTO updateStudentDTO) {
        studentMapper.updateById(studentConverter.updateStudentDTOToPojo(updateStudentDTO));
        return ResponseDTO.success("更新成功", null);
    }

    @Override
    public ResponseDTO<QueryStudentVO> getStudentByTeacherId(QueryStudentDTO queryStudentDTO) {
        // 1. 分页查询学生
        IPage<Student> page = getPage(queryStudentDTO);
        queryStudentDTO.setSex(queryStudentDTO.getSex() == -1 ? null : queryStudentDTO.getSex());
        studentMapper.selectStudentByTeacherId(page, queryStudentDTO);
        return injectParentInfo(page);
    }

    @Override
    public ResponseDTO<QueryStudentVO> getStudentByClassId(QueryStudentDTO queryStudentDTO) {

        List<StudentDTO> list = studentMapper.selectStudentByClassId(queryStudentDTO);

        List<StudentVO> voList = studentConverter.dtoListToVOList(list);

        return ResponseDTO.success(new QueryStudentVO(voList, (long) list.size()));
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
    public ResponseDTO<QueryStudentVO> getStudentByInstitutionId(QueryStudentDTO queryStudentDTO) {
        // 1. 分页查询学生
        IPage<Student> page = getPage(queryStudentDTO);
        queryStudentDTO.setSex(queryStudentDTO.getSex() == -1 ? null : queryStudentDTO.getSex());
        studentMapper.selectStudentByInstitutionId(page, queryStudentDTO);
        return injectParentInfo(page);
    }

    @Override
    public ResponseDTO<QueryStudentVO> getStudent(QueryStudentDTO queryStudentDTO) {
        return null;
    }

    private ResponseDTO<QueryStudentVO> injectParentInfo(IPage<Student> page) {
        List<Student> students = page.getRecords();
        if (students.isEmpty()) {
            return ResponseDTO.success(new QueryStudentVO(new ArrayList<>(), 0L));
        }

        // 1. 获取本页所有学生 ID
        List<Long> studentIds = students.stream().map(Student::getId).toList();

        // 2. 一次性查出所有相关的家长记录（包含主次）
        List<ParentVO> allParents = parentStudentMapper.selectAllBatchParents(studentIds);

        // 3. 将家长按学生 ID 分组：Map<学生ID, 家长列表>
        Map<Long, List<ParentVO>> parentGroupMap = allParents.stream()
                .collect(Collectors.groupingBy(ParentVO::getStudentId));

        // 4. 组装 VO 列表
        List<StudentVO> voList = students.stream().map(student -> {
            StudentVO vo = studentConverter.pojoToVO(student);

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

    private IPage<Student> getPage(QueryStudentDTO queryStudentDTO) {
        return new Page<>(
                queryStudentDTO.getCurrentPage() == null ? 1 : queryStudentDTO.getCurrentPage(),
                queryStudentDTO.getPageSize() == null ? 10 : queryStudentDTO.getPageSize()
        );
    }
}




