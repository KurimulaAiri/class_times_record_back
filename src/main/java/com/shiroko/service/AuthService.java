package com.shiroko.service;

import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.dto.auth.LoginDTO;
import com.shiroko.repository.dto.auth.RegisterDTO;
import com.shiroko.repository.vo.auth.LoginVO;
import com.shiroko.repository.vo.auth.RegisterVO;

/**
 * Description: 认证服务接口
 *
 * @author Guguguy
 * @version 2.0
 * @since 2026/3/19 下午4:07
 */
public interface AuthService {

    ResponseDTO<LoginVO> wxLogin(LoginDTO dto);

    ResponseDTO<LoginVO> loginByPwd(LoginDTO dto);

    ResponseDTO<LoginVO> getOpenId(String code);

    ResponseDTO<LoginVO> loginByToken(LoginDTO dto);

    ResponseDTO<LoginVO> refreshAccessToken(String refreshToken);

    ResponseDTO<String> logout(LoginDTO dto);

    ResponseDTO<RegisterVO> register(RegisterDTO dto);
}
