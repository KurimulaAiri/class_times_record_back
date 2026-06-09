package com.shiroko.converter;

import com.shiroko.annotation.BaseDateTimeToString;
import com.shiroko.annotation.BaseDateToString;
import com.shiroko.repository.dto.student.InsertStudentDTO;
import com.shiroko.repository.dto.student.StudentDTO;
import com.shiroko.repository.dto.student.UpdateStudentDTO;
import com.shiroko.repository.entity.Student;
import com.shiroko.repository.vo.student.StudentVO;
import com.shiroko.util.DateTransformUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Description: 学生转换器
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/4/24 上午1:31
 */
@Mapper(componentModel = "spring", uses = {DateTransformUtils.class})
public interface StudentConverter extends BaseConverter<Student, StudentVO> {

    @Mapping(target = "createTimeStr", source = "createTime", qualifiedBy = BaseDateTimeToString.class)
    @Mapping(target = "updateTimeStr", source = "updateTime", qualifiedBy = BaseDateTimeToString.class)
    @Mapping(target = "birthStr", source = "birth", qualifiedBy = BaseDateToString.class)
    @Override
    StudentVO pojoToVO(Student pojo);

    // 1. 显式定义 DTO 到 VO 的转换，这样 MapStruct 就不会去调那个父类的 pojoToVO 了
    @Mapping(target = "relation", source = "relation")
    @Mapping(target = "createTimeStr", source = "createTime", qualifiedBy = BaseDateTimeToString.class)
    @Mapping(target = "updateTimeStr", source = "updateTime", qualifiedBy = BaseDateTimeToString.class)
    @Mapping(target = "birthStr", source = "birth", qualifiedBy = BaseDateToString.class)
    StudentVO dtoToVO(StudentDTO dto);

    List<StudentVO> dtoListToVOList(List<StudentDTO> dtoList);

    Student updateStudentDTOToPojo(UpdateStudentDTO dto);

    Student insertStudentDTOToPojo(InsertStudentDTO dto);
}
