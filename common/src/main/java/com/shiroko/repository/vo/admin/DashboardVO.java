package com.shiroko.repository.vo.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardVO {

    private Long studentCount;

    private Long teacherCount;

    private Long institutionCount;

    private Long courseCount;

    private Long classCount;
}
