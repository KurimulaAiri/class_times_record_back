package com.shiroko.repository.vo.admin.record;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AdminRecordListVO {
    private List<AdminRecordVO> list;
    private Long total;
}
