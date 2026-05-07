package com.shiroko.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiroko.converter.StudentConverter;
import com.shiroko.mapper.InstitutionMapper;
import com.shiroko.mapper.ParentStudentMapper;
import com.shiroko.mapper.StudentMapper;
import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.dto.student.QueryStudentDTO;
import com.shiroko.repository.dto.student.StudentDTO;
import com.shiroko.repository.dto.student.UpdateStudentDTO;
import com.shiroko.repository.entity.Student;
import com.shiroko.repository.vo.parent.ParentVO;
import com.shiroko.repository.vo.student.QueryStudentVO;
import com.shiroko.repository.vo.student.StudentVO;
import com.shiroko.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    private final StudentConverter studentConverter;

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
        IPage<Student> page = new Page<>(
                queryStudentDTO.getCurrentPage() == null ? 1 : queryStudentDTO.getCurrentPage(),
                queryStudentDTO.getPageSize() == null ? 10 : queryStudentDTO.getPageSize()
        );
        studentMapper.selectStudentByTeacherId(page, queryStudentDTO);

        List<Student> students = page.getRecords();
        if (students.isEmpty()) {
            return ResponseDTO.success(new QueryStudentVO(new ArrayList<>(), 0L));
        }

        // 2. 批量获取学生ID
        List<Long> studentIds = students.stream().map(Student::getId).collect(Collectors.toList());

        // 3. 一次性查询这些学生的所有“主要家长”信息（减少数据库交互）
        // 这里的 selectPrimaryParentMap 建议在 Mapper 中通过 JOIN 一次性查出 ps.relation 和 p.phone
        List<ParentVO> parentVOs = parentStudentMapper.selectBatchPrimaryParents(studentIds);

        // 转为 Map <studentId, ParentVO>
        Map<Long, ParentVO> parentMap = parentVOs.stream()
                .collect(Collectors.toMap(ParentVO::getStudentId, vo -> vo, (v1, v2) -> v1));

        // 4. 组装最终展示的 VO
        List<StudentVO> voList = students.stream().map(student -> {
            StudentVO vo = studentConverter.pojoToVO(student);
            // 从 Map 中直接获取，不再查数据库
            vo.setPrimaryParent(parentMap.get(vo.getId()));
            return vo;
        }).collect(Collectors.toList());

        return ResponseDTO.success(new QueryStudentVO(voList, page.getTotal()));
    }

    @Override
    public ResponseDTO<QueryStudentVO> getStudentByClassId(QueryStudentDTO queryStudentDTO) {

        List<StudentDTO> list = studentMapper.selectStudentByClassId(queryStudentDTO);

        List<StudentVO> voList = studentConverter.dtoListToVOList(list);

        return ResponseDTO.success(new QueryStudentVO(voList, (long) list.size()));
    }
}




