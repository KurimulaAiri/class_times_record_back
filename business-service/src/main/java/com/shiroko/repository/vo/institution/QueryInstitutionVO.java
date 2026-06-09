package com.shiroko.repository.vo.institution;

import com.shiroko.repository.entity.Institution;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Description: 查询机构VO类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/4 下午4:50
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryInstitutionVO {
    private List<InstitutionVO> institutions;
}
