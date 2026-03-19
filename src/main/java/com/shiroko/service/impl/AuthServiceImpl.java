package com.shiroko.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.vo.LoginVO;
import com.shiroko.service.AuthService;
import com.shiroko.service.UserService;
import com.shiroko.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Description: TODO
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/3/19 下午4:13
 */
@Service
public class AuthServiceImpl implements AuthService {
    @Value("${uni-app.wx.app-id}")
    private String appid;

    @Value("${uni-app.wx.secret}")
    private String secret;

    private final RestTemplate restTemplate;
    private final JwtUtils jwtUtils;
    private final UserService userService;

    @Autowired
    private AuthServiceImpl(JwtUtils jwtUtils, UserService userService) {
        this.restTemplate = new RestTemplate();
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }


    @Override
    public ResponseDTO<LoginVO> wxLogin(String code) {
        // 1. 拼接微信 API 地址
        String url = String.format(
                "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
                appid, secret, code
        );

        // 2. 发起请求
        String response = restTemplate.getForObject(url, String.class);
        JSONObject jsonObject = JSON.parseObject(response);

        // 3. 检查微信返回结果
        String openid = jsonObject.getString("openid");
        if (openid == null) {
            return ResponseDTO.fail("微信登录失败：" + jsonObject.getString("errmsg"));
        }

        // 4. 业务逻辑：根据 openid 去数据库查用户，没有则注册，有则更新
        Long userId = userService.saveOrUpdateUser(openid);

        // 5. 生成自定义 Token (推荐 JWT)
        String token = jwtUtils.createToken(userId);

        return ResponseDTO.success(new LoginVO(token, openid));

    }
}
