package com.shiroko.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiroko.mapper.TeacherMapper;
import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.dto.teacher.QueryTeacherDTO;
import com.shiroko.repository.entity.Teacher;
import com.shiroko.repository.vo.teacher.QueryTeacherVO;
import com.shiroko.repository.vo.teacher.TeacherVO;
import com.shiroko.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 教师服务实现
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/4/15 下午22:37
 */
@Service("teacherService")
@RequiredArgsConstructor
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService{

    private final TeacherMapper teacherMapper;

    @Override
    public ResponseDTO<QueryTeacherVO> getTeacherById(QueryTeacherDTO queryTeacherDTO) {

        TeacherVO teacherVO = teacherMapper.getTeacherById(queryTeacherDTO.getTeacherId());

        QueryTeacherVO queryTeacherVO = new QueryTeacherVO();
        List<TeacherVO> teachers = new ArrayList<>();
        teachers.add(teacherVO);
        queryTeacherVO.setTeachers(teachers);
        queryTeacherVO.setTotal(1L);

        return ResponseDTO.success(queryTeacherVO);
    }

    @Override
    public ResponseDTO<QueryTeacherVO> getTeacherByInstitutionId(QueryTeacherDTO queryTeacherDTO) {
        List<TeacherVO> teachers = teacherMapper.getTeacherByInstitutionId(queryTeacherDTO.getInstitutionId());

        QueryTeacherVO queryTeacherVO = new QueryTeacherVO();
        queryTeacherVO.setTeachers(teachers);
        queryTeacherVO.setTotal((long) teachers.size());

        return ResponseDTO.success(queryTeacherVO);
    }
}




