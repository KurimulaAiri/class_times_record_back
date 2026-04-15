package com.shiroko.controller;



import com.shiroko.repository.dto.LoginDTO;
import com.shiroko.repository.dto.RegisterDTO;
import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.vo.LoginVO;
import com.shiroko.repository.vo.RegisterVO;
import com.shiroko.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description: 认证控制器：处理微信小程序登录鉴权
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/3/19 下午4:03
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/get_open_id")
    public ResponseDTO<LoginVO> getOpenId(@Valid @RequestBody LoginDTO dto) {
        return authService.getOpenId(dto.getCode());
    }

    /**
     * 微信登录
     * @param dto 包含小程序端传来的 code
     * @return 包含自定义 Token 的响应
     */
    @PostMapping("/login_no_pwd")
    public ResponseDTO<LoginVO> login(@Valid @RequestBody LoginDTO dto) {
        return authService.wxLogin(dto.getCode());
    }

    @PostMapping("/login_by_pwd")
    public ResponseDTO<LoginVO> loginByPwd(@Valid @RequestBody LoginDTO dto) {
        return authService.loginByPwd(dto);
    }

    @PostMapping("/register")
    public ResponseDTO<RegisterVO> register(@Valid @RequestBody RegisterDTO dto) {
        return authService.register(dto);
    }
}