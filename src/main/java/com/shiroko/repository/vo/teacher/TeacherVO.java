package com.shiroko.repository.vo.teacher;

import com.baomidou.mybatisplus.annotation.TableField;
import com.shiroko.repository.entity.Institution;
import lombok.Data;

/**
 * Description: 教师VO类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/7 下午6:32
 */
@Data
public class TeacherVO {

    private Long teacherId;

    @TableField(exist = false)
    private Institution institution;

    private Boolean isAdmin;

    private Boolean isAvailable;

    private String username;

}
