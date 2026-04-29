package com.shiroko.converter;

import com.shiroko.repository.dto.student.UpdateStudentDTO;
import com.shiroko.repository.entity.Student;
import com.shiroko.repository.vo.student.StudentVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Description: 学生转换器
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/4/24 上午1:31
 */
@Mapper(componentModel = "spring")
public interface StudentConverter extends BaseConverter<Student, StudentVO> {

    @Mapping(target = "createTimeStr", source = "createTime" , qualifiedByName = "dateToString")
    @Mapping(target = "updateTimeStr", source = "updateTime", qualifiedByName = "dateToString")
    @Mapping(target = "birthStr", source = "birth", qualifiedByName = "dateToString")
    @Override
    StudentVO pojoToVO(Student pojo);

    Student updateStudentDTOToPojo(UpdateStudentDTO dto);

}
