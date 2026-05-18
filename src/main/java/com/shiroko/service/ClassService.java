package com.shiroko.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.dto.clazz.InsertClassDTO;
import com.shiroko.repository.dto.clazz.QueryClassDTO;
import com.shiroko.repository.dto.clazz.UpdateClassDTO;
import com.shiroko.repository.entity.Class;
import com.shiroko.repository.vo.clazz.InsertClassVO;
import com.shiroko.repository.vo.clazz.QueryClassVO;
import com.shiroko.repository.vo.clazz.UpdateClassVO;

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

    ResponseDTO<UpdateClassVO> addStudentToClass(UpdateClassDTO updateClassDTO);

    ResponseDTO<QueryClassVO> getClassById(QueryClassDTO queryClassDTO);

    ResponseDTO<InsertClassVO> insertClass(InsertClassDTO insertClassDTO);

    ResponseDTO<UpdateClassVO> removeStudentFromClass(UpdateClassDTO updateClassDTO);

    ResponseDTO<UpdateClassVO> updateClassByClassId(UpdateClassDTO updateClassDTO);
}
