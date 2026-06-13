package com.shiroko.repository.vo.admin.student;

import com.shiroko.repository.vo.parent.ParentVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminStudentVO {
    private Long id;
    private String avatar;
    private String studentName;
    private Long institutionId;
    private Long sex;
    private String birthStr;
    private String school;
    private String address;
    private String createTimeStr;
    private String updateTimeStr;
    private ParentVO primaryParent;
    private ParentVO secondaryParent;
}
