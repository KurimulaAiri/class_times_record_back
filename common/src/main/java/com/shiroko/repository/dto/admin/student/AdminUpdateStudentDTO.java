package com.shiroko.repository.dto.admin.student;

import lombok.Data;

@Data
public class AdminUpdateStudentDTO {
    private Long id;
    private String studentName;
    private Long sex;
    private String birth;
    private String school;
    private String address;
}
