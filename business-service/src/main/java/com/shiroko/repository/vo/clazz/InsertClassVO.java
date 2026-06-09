package com.shiroko.repository.vo.clazz;

import lombok.Data;

/**
 * Description: 班级插入VO类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/17 上午10:52
 */
@Data
public class InsertClassVO {

    /**
     * 班级名称
     */
    private String className;

    /**
     * 班级 ID
     */
    private Long classId;
}
