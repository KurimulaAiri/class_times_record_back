package com.shiroko.repository.vo.admin.clazz;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AdminClassListVO {
    private List<AdminClassVO> list;
    private Long total;
}
