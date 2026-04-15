package com.shiroko.service;

import com.shiroko.repository.dto.LoginDTO;
import com.shiroko.repository.dto.RegisterDTO;
import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.vo.LoginVO;
import com.shiroko.repository.vo.RegisterVO;

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

    ResponseDTO<RegisterVO> register(RegisterDTO dto);
}
