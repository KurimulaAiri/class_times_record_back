package com.shiroko.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiroko.repository.entity.Student;
import com.shiroko.service.StudentService;
import com.shiroko.mapper.StudentMapper;
import org.springframework.stereotype.Service;

/**
 * Description: 学生数据库实体类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/4/23 上午14:32
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student>
    implements StudentService{

}




