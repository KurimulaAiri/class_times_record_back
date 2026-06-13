package com.shiroko.repository.dto.admin.institution;

import lombok.Data;

@Data
public class AdminUpdateInstitutionDTO {
    private Long id;
    private String institutionName;
    private String institutionAddress;
    private String institutionCode;
    private Long status;
}
