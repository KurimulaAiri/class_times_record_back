package com.shiroko.repository.dto.institution;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Description: 学校DTO类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/6/9 下午4:33
 */
@Data
public class InstitutionDTO {
    private Long id;
    private String institutionName;
    private String institutionAddress;
    private String institutionCode;
    private Long status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

}
