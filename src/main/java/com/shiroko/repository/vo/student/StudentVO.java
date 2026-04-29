package com.shiroko.repository.vo.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private String studentName;

    private String birthStr;

    private String school;

    private String address;

    private String createTimeStr;

    private String updateTimeStr;

}
