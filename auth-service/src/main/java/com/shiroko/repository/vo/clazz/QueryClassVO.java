package com.shiroko.repository.vo.clazz;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Description: 查询班级VO类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/7 下午9:05
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryClassVO {

    private List<ClassVO> classList;

    private Long total;

}
