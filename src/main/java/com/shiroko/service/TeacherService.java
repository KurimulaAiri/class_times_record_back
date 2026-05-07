package com.shiroko.service;

import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.dto.teacher.QueryTeacherDTO;
import com.shiroko.repository.entity.Teacher;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shiroko.repository.vo.teacher.QueryTeacherVO;

/**
 * Description: 教师服务接口
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/4/15 下午22:37
 */
public interface TeacherService extends IService<Teacher> {

    ResponseDTO<QueryTeacherVO> getTeacherById(QueryTeacherDTO queryTeacherDTO);
}
