package com.shiroko.repository.dto.institution;

import lombok.Data;

/**
 * Description: 更新机构DTO
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/31 下午2:59
 */
@Data
public class UpdateInstitutionDTO {
    private Long institutionId;
    private String institutionName;
    private String institutionAddress;
}
