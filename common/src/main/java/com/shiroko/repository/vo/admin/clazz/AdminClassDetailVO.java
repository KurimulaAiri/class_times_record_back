package com.shiroko.repository.vo.admin.clazz;

import com.shiroko.repository.entity.Class;
import com.shiroko.repository.entity.Student;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AdminClassDetailVO {
    private List<Class> classList;
    private List<Student> students;
}
