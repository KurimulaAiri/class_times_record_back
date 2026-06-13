package com.shiroko.repository.dto.admin.clazz;

import lombok.Data;

import java.util.List;

@Data
public class AdminClassStudentDTO {
    private Long classId;
    private List<StudentRef> students;

    @Data
    public static class StudentRef {
        private Long id;
    }
}
