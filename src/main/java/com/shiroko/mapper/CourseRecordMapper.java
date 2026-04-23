package com.shiroko.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shiroko.repository.dto.QueryCourseRecordDTO;
import com.shiroko.repository.entity.CourseRecord;
import com.shiroko.repository.vo.CourseRecordVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
    /**
     * 使用 IPage 进行分页查询，MP 会自动处理 limit 和 count
     */
    IPage<CourseRecord> selectCourseCustomPage(
            IPage<CourseRecord> page,
            @Param("dto") QueryCourseRecordDTO dto
    );
}
