package com.shiroko.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shiroko.repository.entity.ParentStudent;
import com.shiroko.repository.vo.parent.ParentVO;

import java.util.List;

/**
 * Description: 家长学生数据库操作接口
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/7 上午1:13
 */
public interface ParentStudentMapper extends BaseMapper<ParentStudent> {

    List<ParentVO> selectBatchPrimaryParents(List<Long> studentIds);
}




