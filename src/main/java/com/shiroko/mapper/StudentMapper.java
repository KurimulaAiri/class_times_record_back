package com.shiroko.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shiroko.repository.dto.QueryStudentDTO;
import com.shiroko.repository.entity.Student;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
    IPage<Student> selectStudentByParentId(IPage<Student> page,@Param("dto") QueryStudentDTO queryStudentDTO);

}




