package com.shiroko.repository.vo.student;

import com.shiroko.repository.entity.Institution;
import com.shiroko.repository.vo.parent.ParentVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Description: 学生VO类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/4/24 上午1:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentVO{

    private Long id;

    private String avatar;

    private String studentName;

    private Long sex;

    private String relation;

    private String birthStr;

    private String school;

    private String address;

    private List<Institution> institutions;

    private ParentVO primaryParent;

    private ParentVO secondaryParent;

    private Long courseTotalTime;

    private Long courseRestTime;

    private String createTimeStr;

    private String updateTimeStr;

}
