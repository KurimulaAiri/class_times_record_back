package com.shiroko.service;

import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.vo.LoginVO;

/**
 * Description: TODO
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/3/19 下午4:07
 */
public interface AuthService {

    ResponseDTO<LoginVO> wxLogin(String code);

}
