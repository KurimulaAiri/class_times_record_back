package com.shiroko.repository.vo.admin.institution;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AdminInstitutionListVO {
    private List<AdminInstitutionVO> list;
    private Long total;
}
