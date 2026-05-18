package com.shiroko.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shiroko.repository.dto.clazz.UpdateClassDTO;
import com.shiroko.repository.entity.ClassStudent;
import org.apache.ibatis.annotations.Param;

/**
 * Description: 班级学生映射接口
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/7 下午8:53
 */
public interface ClassStudentMapper extends BaseMapper<ClassStudent> {
    Long insertBatch(@Param("dto") UpdateClassDTO dto);

    Long deleteBatch(@Param("dto") UpdateClassDTO dto);
}
