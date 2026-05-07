package com.shiroko.repository.vo.clazz;

import lombok.Data;

import java.util.List;

/**
 * Description: 查询班级VO类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/7 下午9:05
 */
@Data
public class QueryClassVO {

    private List<ClassVO> classList;

    private Long total;

}
