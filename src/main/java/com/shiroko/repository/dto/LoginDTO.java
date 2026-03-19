package com.shiroko.repository.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description: TODO
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/3/19 下午4:10
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginDTO {
    @NotBlank(message = "code不能为空")
    private String code;
}
