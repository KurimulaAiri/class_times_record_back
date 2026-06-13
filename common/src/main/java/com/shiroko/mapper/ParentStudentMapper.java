package com.shiroko.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shiroko.repository.entity.ParentStudent;
import com.shiroko.repository.vo.parent.ParentVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Description: 家长学生数据库操作接口
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/7 上午1:13
 */
public interface ParentStudentMapper extends BaseMapper<ParentStudent> {

    /**
     * 一次性批量查询学生的所有家长（不区分主次）
     */
    List<ParentVO> selectAllBatchParents(@Param("studentIds") List<Long> studentIds);

}
