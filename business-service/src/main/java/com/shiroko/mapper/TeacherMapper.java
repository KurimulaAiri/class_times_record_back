package com.shiroko.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shiroko.repository.entity.Teacher;
import com.shiroko.repository.vo.teacher.TeacherVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Description: 教师Mapper接口
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/4/15 下午22:37
 */
public interface TeacherMapper extends BaseMapper<Teacher> {

    TeacherVO getTeacherById(Long teacherId);

    List<TeacherVO> getTeacherByInstitutionId(Long institutionId);

    Long updateBatchById(@Param("list") List<Teacher> list);
}




