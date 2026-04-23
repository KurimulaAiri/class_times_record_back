package com.shiroko.service;

import com.shiroko.repository.dto.QueryStudentDTO;
import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.entity.Student;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shiroko.repository.vo.QueryStudentVO;

/**
 * Description: 学生数据库实体类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/4/23 上午14:32
 */
public interface StudentService extends IService<Student> {

    ResponseDTO<QueryStudentVO> getStudentByParentId(QueryStudentDTO queryStudentDTO);

}
