package com.shiroko.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiroko.annotation.UpdateStudentCount;
import com.shiroko.converter.ClassConverter;
import com.shiroko.mapper.ClassMapper;
import com.shiroko.mapper.ClassStudentMapper;
import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.dto.clazz.ClassDTO;
import com.shiroko.repository.dto.clazz.QueryClassDTO;
import com.shiroko.repository.dto.clazz.UpdateClassDTO;
import com.shiroko.repository.entity.Class;
import com.shiroko.repository.vo.clazz.ClassVO;
import com.shiroko.repository.vo.clazz.QueryClassVO;
import com.shiroko.repository.vo.clazz.UpdateClassVO;
import com.shiroko.service.ClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Description: 班级服务实现
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/7 下午20:27
 */
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class ClassServiceImpl extends ServiceImpl<ClassMapper, Class> implements ClassService{

    private final ClassMapper classMapper;
    private final ClassConverter classConverter;
    private final ClassStudentMapper classStudentMapper;

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

        // System.out.println("classList: " + classList);

        List<ClassVO> classVOList = classConverter.dtoListToVOList(classList);

        // System.out.println("classVOList: " + classVOList);

        QueryClassVO queryClassVO = new QueryClassVO();
        queryClassVO.setClassList(classVOList);
        queryClassVO.setTotal((long) classList.size());

        return ResponseDTO.success(queryClassVO);
    }

    @Override
    @UpdateStudentCount
    public ResponseDTO<UpdateClassVO> addStudentToClass(UpdateClassDTO updateClassDTO) {
        Long result;
        try {
            result = classStudentMapper.insertBatch(updateClassDTO);
        } catch (DuplicateKeyException e) {
            return ResponseDTO.fail("班级中已存在该学生");
        }
        return ResponseDTO.success(new UpdateClassVO(result));
    }

    @Override
    public ResponseDTO<QueryClassVO> getClassById(QueryClassDTO queryClassDTO) {
        ClassDTO clazz = classMapper.selectByClassId(queryClassDTO);
        if (clazz == null) {
            return ResponseDTO.fail("班级不存在");
        }
        return ResponseDTO.success(new QueryClassVO(List.of(classConverter.dtoToVo(clazz)), 1L));
    }
}




