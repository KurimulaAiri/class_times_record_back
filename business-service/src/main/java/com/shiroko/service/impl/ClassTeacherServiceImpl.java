package com.shiroko.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiroko.mapper.ClassTeacherMapper;
import com.shiroko.repository.entity.ClassTeacher;
import com.shiroko.service.ClassTeacherService;
import org.springframework.stereotype.Service;

/**
 * Description: 班级与老师的关联表服务实现
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/18 上午10:21
 */
@Service
public class ClassTeacherServiceImpl extends ServiceImpl<ClassTeacherMapper, ClassTeacher>
        implements ClassTeacherService {

}




