package com.shiroko.repository.dto.admin.student;

import lombok.Data;

@Data
public class AdminInsertStudentDTO {
    private String studentName;
    private Long institutionId;
    private Long sex;
    private String birth;
    private String school;
    private String address;
}
