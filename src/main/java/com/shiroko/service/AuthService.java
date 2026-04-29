package com.shiroko.service;

import com.shiroko.repository.dto.auth.LoginDTO;
import com.shiroko.repository.dto.auth.RegisterDTO;
import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.vo.auth.LoginVO;
import com.shiroko.repository.vo.auth.RegisterVO;

/**
 * Description: TODO
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/3/19 下午4:07
 */
public interface AuthService {

    ResponseDTO<LoginVO> wxLogin(String code);

    ResponseDTO<LoginVO> loginByPwd(LoginDTO dto);

    ResponseDTO<LoginVO> getOpenId(String code);

    ResponseDTO<LoginVO> loginByToken(LoginDTO dto);

    ResponseDTO<String> logout(LoginDTO dto);

    ResponseDTO<RegisterVO> register(RegisterDTO dto);
}
