package com.shiroko.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shiroko.repository.dto.clazz.ClassDTO;
import com.shiroko.repository.dto.clazz.QueryClassDTO;
import com.shiroko.repository.entity.Class;

import java.util.List;

/**
 * Description: 班级数据库操作接口
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/7 下午20:27
 */
public interface ClassMapper extends BaseMapper<Class> {

    /**
     * Description: 根据学生id查询班级
     *
     * @param queryClassDTO 查询班级dto
     * @return 班级列表
     */
    List<ClassDTO> getClassesByStudentId(QueryClassDTO queryClassDTO);

    List<ClassDTO> getClassesByTeacherId(QueryClassDTO queryClassDTO);

    ClassDTO selectByClassId(QueryClassDTO queryClassDTO);

    List<ClassDTO> getClassesByInstitutionId(QueryClassDTO queryClassDTO);
}




