package com.shiroko.repository.dto.admin.institution;

import lombok.Data;

@Data
public class InsertInstitutionDTO {
    private String institutionName;
    private String institutionAddress;
    private String institutionCode;
}
