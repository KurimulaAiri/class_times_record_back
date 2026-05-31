package com.shiroko.repository.vo.institution;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description: 机构VO
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/31 下午3:13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InstitutionVO {

    /**
     * 机构id
     */
    private Long id;

    /**
     * 机构名称
     */
    private String institutionName;

    /**
     * 机构地址
     */
    private String institutionAddress;

    /**
     * 机构代码
     */
    private String institutionCode;


    /**
     * 机构状态：
     * 0：待审核；
     * 1：启用；
     * 2：禁用；
     */
    private Long status;

    /**
     * 创建时间
     */
    private String createTimeStr;



    /**
     * 更新时间
     */
    private String updateTimeStr;

}
