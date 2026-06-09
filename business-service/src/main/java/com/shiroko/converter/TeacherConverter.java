package com.shiroko.converter;

import com.shiroko.repository.dto.teacher.InsertTeacherDTO;
import com.shiroko.repository.dto.teacher.UpdateTeacherDTO;
import com.shiroko.repository.entity.Teacher;
import com.shiroko.repository.vo.teacher.TeacherVO;
import org.mapstruct.Mapper;

/**
 * Description: 教师转换器
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/6/7 下午1:34
 */
@Mapper(componentModel = "spring")
public interface TeacherConverter extends BaseConverter<Teacher, TeacherVO> {

    Teacher updateDTOToPOJO(UpdateTeacherDTO updateTeacherDTO);

    Teacher insertDTOToPOJO(InsertTeacherDTO insertTeacherDTO);

}
