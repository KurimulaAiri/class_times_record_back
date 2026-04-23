package com.shiroko.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiroko.converter.StudentConverter;
import com.shiroko.repository.dto.QueryStudentDTO;
import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.entity.Student;
import com.shiroko.repository.vo.QueryStudentVO;
import com.shiroko.service.StudentService;
import com.shiroko.mapper.StudentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description: 学生数据库实体类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/4/23 上午14:32
 */
@Service
@RequiredArgsConstructor
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

    private final StudentMapper studentMapper;

    private final StudentConverter studentConverter;

    @Override
    public ResponseDTO<QueryStudentVO> getStudentByParentId(QueryStudentDTO queryStudentDTO) {

        IPage<Student> page = new Page<>(
                queryStudentDTO.getCurrentPage() == null ? 1 : queryStudentDTO.getCurrentPage(),
                queryStudentDTO.getPageSize() == null ? 10 : queryStudentDTO.getPageSize()
        );

        studentMapper.selectStudentByParentId(page, queryStudentDTO);

        List<Student> list = page.getRecords();

        return ResponseDTO.success(new QueryStudentVO(studentConverter.pojoListToVOList(list), page.getTotal()));
    }
}




