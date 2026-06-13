package com.shiroko.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shiroko.repository.entity.Institution;

import java.util.List;

/**
 * Description: 机构Mapper接口
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/4 下午16:43
 */
public interface InstitutionMapper extends BaseMapper<Institution> {

    List<Institution> selectListByStudentId(Long studentId);

    List<Institution> selectListByOpenId(String platform, String openId);
}
