package com.shiroko.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiroko.converter.ClassConverter;
import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.dto.clazz.ClassDTO;
import com.shiroko.repository.dto.clazz.QueryClassDTO;
import com.shiroko.repository.entity.Class;
import com.shiroko.repository.vo.clazz.ClassVO;
import com.shiroko.repository.vo.clazz.QueryClassVO;
import com.shiroko.service.ClassService;
import com.shiroko.mapper.ClassMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description: 班级服务实现
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/7 下午20:27
 */
@Service
@RequiredArgsConstructor
public class ClassServiceImpl extends ServiceImpl<ClassMapper, Class> implements ClassService{

    private final ClassMapper classMapper;
    private final ClassConverter classConverter;

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

        System.out.println("classList: " + classList);

        List<ClassVO> classVOList = classConverter.dtoListToVOList(classList);

        System.out.println("classVOList: " + classVOList);

        QueryClassVO queryClassVO = new QueryClassVO();
        queryClassVO.setClassList(classVOList);
        queryClassVO.setTotal((long) classList.size());

        return ResponseDTO.success(queryClassVO);
    }
}




