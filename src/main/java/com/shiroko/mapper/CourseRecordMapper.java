package com.shiroko.mapper;

/**
 * Description: TODO
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/2/7 上午12:51
 */
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shiroko.repository.entity.CourseRecord;
import org.apache.ibatis.annotations.Mapper;

import java.io.Serializable;
import java.util.List;

/**
 * Description: TODO
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/2/7 上午12:51
 */
@Mapper
public interface CourseRecordMapper extends BaseMapper<CourseRecord> {
}
