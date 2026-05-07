package com.shiroko.service;

import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.dto.clazz.QueryClassDTO;
import com.shiroko.repository.entity.Class;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shiroko.repository.vo.clazz.QueryClassVO;

/**
 * Description: 班级服务接口
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/7 下午20:27
 */
public interface ClassService extends IService<Class> {

    ResponseDTO<QueryClassVO> getClassesByStudentId(QueryClassDTO queryClassDTO);

    ResponseDTO<QueryClassVO> getClassesByTeacherId(QueryClassDTO queryClassDTO);
}
