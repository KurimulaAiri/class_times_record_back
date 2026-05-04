package com.shiroko.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiroko.converter.StudentConverter;
import com.shiroko.mapper.InstitutionMapper;
import com.shiroko.mapper.StudentMapper;
import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.dto.student.QueryStudentDTO;
import com.shiroko.repository.dto.student.StudentDTO;
import com.shiroko.repository.dto.student.UpdateStudentDTO;
import com.shiroko.repository.entity.Student;
import com.shiroko.repository.vo.student.QueryStudentVO;
import com.shiroko.repository.vo.student.StudentVO;
import com.shiroko.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
}




