package com.shiroko.repository.vo.admin.institution;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminInstitutionVO {
    private Long id;
    private String institutionName;
    private String institutionAddress;
    private String institutionCode;
    private Long status;
    private String createTimeStr;
    private String updateTimeStr;
}
