package com.shiroko.repository.dto.institution;

import com.shiroko.repository.dto.institution.validategroup.QueryGroup;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Description: 查询机构dto类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/4 下午4:49
 */
@Data
public class QueryInstitutionDTO {

    @NotNull(message = "institutionId不能为空", groups = QueryGroup.ById.class)
    private Long institutionId;

    private Long studentId;

    @NotNull(message = "institutionCode不能为空", groups = QueryGroup.ByInstitutionCode.class)
    private String institutionCode;

    @NotNull(message = "openId不能为空", groups = QueryGroup.ByOpenId.class)
    private String openId;

    @NotNull(message = "platform不能为空", groups = QueryGroup.ByOpenId.class)
    private String platform;
}
