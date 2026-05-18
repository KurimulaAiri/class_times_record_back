package com.shiroko.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shiroko.repository.entity.ClassTeacher;

import java.util.List;

/**
 * Description: 班级与老师的关联表映射接口
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/18 上午10:21
 */
public interface ClassTeacherMapper extends BaseMapper<ClassTeacher> {

    Long insertBatch(List<ClassTeacher> classTeacherList);

}




