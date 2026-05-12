package com.shiroko.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shiroko.repository.dto.student.QueryStudentDTO;
import com.shiroko.repository.dto.student.StudentDTO;
import com.shiroko.repository.entity.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Description: 学生数据库操作接口
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/4/23 上午14:32
 */
@Mapper
public interface StudentMapper extends BaseMapper<Student> {

    /**
     * 根据家长id查询学生列表
     * @param page 分页参数
     * @param queryStudentDTO 查询参数
     * @return 学生列表
     */
    IPage<StudentDTO> selectStudentByParentId(IPage<StudentDTO> page, @Param("dto") QueryStudentDTO queryStudentDTO);

    /**
     * 根据教师id查询学生列表
     *
     * @param page            分页参数
     * @param queryStudentDTO 查询参数
     * @return 学生列表
     */
    IPage<Student> selectStudentByTeacherId(IPage<Student> page, @Param("dto") QueryStudentDTO queryStudentDTO);

    List<StudentDTO> selectStudentByClassId(@Param("dto") QueryStudentDTO queryStudentDTO);

    /**
     * 根据机构id查询学生列表
     *
     * @param page            分页参数
     * @param queryStudentDTO 查询参数
     * @return 学生列表
     */
    IPage<Student> selectStudentByInstitutionId(IPage<Student> page, @Param("dto") QueryStudentDTO queryStudentDTO);

    Student selectByStudentId(@Param("dto") QueryStudentDTO dto);
}




