package com.shiroko.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shiroko.context.UserContext;
import com.shiroko.feign.IdentityFeignClient;
import com.shiroko.mapper.UserAuthMapper;
import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.dto.auth.GetUserAuthInfoDTO;
import com.shiroko.repository.dto.auth.LoginDTO;
import com.shiroko.repository.dto.auth.RegisterDTO;
import com.shiroko.repository.entity.Permission;
import com.shiroko.repository.entity.UserAuth;
import com.shiroko.repository.entity.UserPlatform;
import com.shiroko.repository.entity.common.RoleBaseEntity;
import com.shiroko.repository.vo.UserVO;
import com.shiroko.repository.vo.auth.GetUserAuthInfoVO;
import com.shiroko.repository.vo.auth.LoginVO;
import com.shiroko.repository.vo.auth.RegisterVO;
import com.shiroko.service.*;
import com.shiroko.util.JwtUtils;
import com.shiroko.util.SM2Util;
import com.shiroko.util.SM3Util;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserAuthMapper userAuthMapper;
    @Value("${uni-app.wx.app-id}")
    private String appid;

    @Value("${uni-app.wx.secret}")
    private String secret;

    @Value("${crypto.sm2.private-key}")
    private String privateKey;

    private final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final RestTemplate restTemplate = new RestTemplate();
    private final JwtUtils jwtUtils;

    private final UserService userService;
    private final UserPlatformService userPlatformService;
    private final PermissionService permissionService;
    private final UserAuthService userAuthService;

    private final IdentityFeignClient identityFeignClient;

    @Override
    public ResponseDTO<LoginVO> wxLogin(LoginDTO dto) {
        ResponseDTO<LoginVO> openidResponse = getOpenId(dto.getCode());
        if (openidResponse.getCode() != 200) {
            return openidResponse;
        }
        String openId = openidResponse.getData().getOpenId();

        UserPlatform platformRecord = userService.saveOrUpdateUser(openId, dto.getPlatform());

        String accessToken = jwtUtils.createAccessToken(platformRecord.getUserId(), dto.getRole());
        String refreshToken = jwtUtils.createRefreshToken(platformRecord.getUserId(), dto.getRole());

        return ResponseDTO.success(new LoginVO(accessToken, refreshToken, openId, null));

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<LoginVO> loginByPwd(LoginDTO dto) {
        String openId = dto.getOpenId();
        Long role = dto.getRole();
        String account = dto.getAccount();
        Long institutionId = dto.getInstitutionId();
        String platform = dto.getPlatform();

        if (institutionId == null || institutionId <= 0) {
            return ResponseDTO.fail("请选择或添加登录机构");
        }

        String password = SM2Util.decrypt(dto.getPassword(), privateKey);

        Permission permission = permissionService.getById(role);
        if (permission == null) {
            return ResponseDTO.fail("非法角色参数");
        }

        UserAuth userAuth = validAuth(account, role, institutionId);
        if (userAuth == null) {
            return ResponseDTO.fail("该机构下不存在当前账号或身份不匹配");
        } else {
            userAuth.setLastLoginTime(LocalDateTime.now());
            userAuthService.updateById(userAuth);
        }

        String salt = userAuth.getSalt();
        String encryptedPassword = SM3Util.digestWithSalt(password, salt);
        if (!encryptedPassword.equals(userAuth.getPassword())) {
            return ResponseDTO.fail("账号或密码错误");
        }

        UserVO<RoleBaseEntity> user = userService.getFullUserInfoByUserId(userAuth.getUserId(), permission);
        if (user == null) {
            return ResponseDTO.fail("用户账户异常");
        }

        if (!hasRolePermission(user.getUserId(), role, permission)) {
            return ResponseDTO.fail("用户身份不可用（当前用户未注册该身份或该身份被禁用）");
        }

        UserPlatform existRecord = userPlatformService.getOne(
                new LambdaQueryWrapper<UserPlatform>()
                        .eq(UserPlatform::getPlatform, platform)
                        .eq(UserPlatform::getOpenId, openId)
                        .eq(UserPlatform::getUserId, user.getUserId())
        );

        if (existRecord == null) {
            UserPlatform newPlatform = new UserPlatform();
            newPlatform.setUserId(user.getUserId());
            newPlatform.setPlatform(platform);
            newPlatform.setOpenId(openId);
            newPlatform.setIsAvailable(true);

            try {
                userPlatformService.save(newPlatform);
            } catch (DuplicateKeyException e) {
                UserPlatform concurrentRecord = userPlatformService.getOne(
                        new LambdaQueryWrapper<UserPlatform>()
                                .eq(UserPlatform::getPlatform, platform)
                                .eq(UserPlatform::getOpenId, openId)
                                .eq(UserPlatform::getUserId, user.getUserId())
                );
                if (concurrentRecord != null && !concurrentRecord.getIsAvailable()) {
                    return ResponseDTO.fail("当前微信登录权限已被禁用，请联系管理员");
                }
            }
        } else {
            if (!existRecord.getIsAvailable()) {
                return ResponseDTO.fail("当前微信登录权限已被禁用，请联系管理员");
            } else {
                existRecord.setPlatform(platform);
                existRecord.setLastLoginTime(LocalDateTime.now());
                existRecord.setLastLoginRole(role);
                userPlatformService.updateById(existRecord);
            }
        }

        String accessToken = jwtUtils.createAccessToken(user.getUserId(), role);
        String refreshToken = jwtUtils.createRefreshToken(user.getUserId(), role);

        return ResponseDTO.success(new LoginVO(accessToken, refreshToken, openId, user));
    }

    @Override
    public ResponseDTO<LoginVO> getOpenId(String code) {
        String url = String.format(
                "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
                appid, secret, code
        );

        String response = restTemplate.getForObject(url, String.class);
        JSONObject jsonObject = JSON.parseObject(response);

        String openid = null;
        if (jsonObject != null) {
            openid = jsonObject.getString("openid");
        }
        if (openid == null) {
            if (jsonObject != null) {
                return ResponseDTO.fail("微信登录失败：" + jsonObject.getString("errmsg"));
            }
        }
        return ResponseDTO.success(new LoginVO(null, null, openid, null));
    }

    @Override
    public ResponseDTO<LoginVO> loginByToken(LoginDTO dto) {
        String token = dto.getToken();
        Map<String, Object> userInfo = jwtUtils.getUserInfoFromToken(token);
        if (userInfo == null) {
            return ResponseDTO.fail("token无效");
        }
        Long userId = Long.parseLong(userInfo.get("userId").toString());
        Long roleId = Long.parseLong(userInfo.get("roleId").toString());

        UserVO<RoleBaseEntity> user = userService.getFullUserInfoByUserId(userId, permissionService.getById(roleId));

        String accessToken = jwtUtils.createAccessToken(userId, roleId);
        String refreshToken = jwtUtils.createRefreshToken(userId, roleId);

        UserAuth userAuth = userAuthService.getOne(new LambdaQueryWrapper<UserAuth>()
                .eq(UserAuth::getUserId, userId)
                .eq(UserAuth::getRoleId, roleId)
        );
        userAuth.setLastLoginTime(LocalDateTime.now());
        userAuthService.updateById(userAuth);

        UserPlatform platformRecord = userPlatformService.getOne(
                new LambdaQueryWrapper<UserPlatform>()
                        .eq(UserPlatform::getUserId, userId)
                        .eq(UserPlatform::getOpenId, dto.getOpenId())
                        .eq(UserPlatform::getPlatform, dto.getPlatform())
        );

        platformRecord.setLastLoginTime(LocalDateTime.now());
        platformRecord.setLastLoginRole(roleId);

        userPlatformService.updateById(platformRecord);

        return ResponseDTO.success(new LoginVO(accessToken, refreshToken, dto.getOpenId(), user));
    }

    @Override
    public ResponseDTO<LoginVO> refreshAccessToken(String refreshToken) {
        Map<String, Object> userInfo = jwtUtils.getUserInfoFromRefreshToken(refreshToken);
        if (userInfo == null) {
            return ResponseDTO.fail("refresh token无效或已过期，请重新登录");
        }
        Long userId = Long.parseLong(userInfo.get("userId").toString());
        Long roleId = Long.parseLong(userInfo.get("roleId").toString());

        String newAccessToken = jwtUtils.createAccessToken(userId, roleId);

        return ResponseDTO.success(new LoginVO(newAccessToken, null, null, null));
    }

    @Override
    public ResponseDTO<String> logout(LoginDTO dto) {
        UserContext.remove();
        return ResponseDTO.success("登出成功");
    }

    @Override
    @Transactional
    public ResponseDTO<RegisterVO> register(RegisterDTO dto) {
        Long userRole = dto.getRole();
        String openId = dto.getOpenId();

        UserPlatform platformRecord = userService.saveOrUpdateUser(openId, dto.getPlatform());
        Long userId = platformRecord.getUserId();

        UserAuth existingUserAuth = userAuthService.getOne(new LambdaQueryWrapper<UserAuth>()
                .eq(UserAuth::getUserId, userId)
                .eq(UserAuth::getRoleId, userRole)
        );
        if (existingUserAuth != null) {
            return ResponseDTO.fail("该openid已注册");
        }

        existingUserAuth = userAuthService.getOne(new LambdaQueryWrapper<UserAuth>()
                .eq(UserAuth::getAccount, dto.getAccount())
                .eq(UserAuth::getRoleId, userRole)
        );
        if (existingUserAuth != null) {
            return ResponseDTO.fail("该账号已注册");
        }

        String rawPassword = SM2Util.decrypt(dto.getPassword(), privateKey);

        String salt = UUID.randomUUID().toString().replace("-", "");
        String hashedPassword = SM3Util.digestWithSalt(rawPassword, salt);

        UserAuth userAuth = new UserAuth();
        userAuth.setUserId(userId);
        userAuth.setAccount(dto.getAccount());
        userAuth.setPassword(hashedPassword);
        userAuth.setSalt(salt);
        userAuth.setRoleId(userRole);

        userAuthService.save(userAuth);

        // 通过 Feign 调用 business-service 创建身份记录
        saveIdentityRecord(userId, dto.getRole());

        return ResponseDTO.success(new RegisterVO(openId));
    }

    @Override
    public ResponseDTO<GetUserAuthInfoVO> getUserAuthByTeacherId(GetUserAuthInfoDTO dto) {
        Long teacherId = dto.getTeacherId();
        UserAuth userAuth = userAuthMapper.selectAuthByTeacherId(teacherId);
        if (userAuth == null) {
            return ResponseDTO.fail("该教师未绑定账号");
        }
        return ResponseDTO.success(new GetUserAuthInfoVO(userAuth.getAccount()));
    }

    /**
     * 通过 Feign 调用 business-service 检查角色权限
     */
    private boolean hasRolePermission(Long userId, Long role, Permission permission) {
        String roleName = permission.getPermissionName();
        ResponseDTO<Boolean> response = identityFeignClient.checkAvailable(
                Map.of("roleName", roleName, "userId", userId)
        );
        return response.getCode() == 200 && Boolean.TRUE.equals(response.getData());
    }

    /**
     * 通过 Feign 调用 business-service 创建身份记录
     */
    private void saveIdentityRecord(Long userId, Long role) {
        Permission permission = permissionService.getById(role);
        String roleName = permission.getPermissionName();

        ResponseDTO<String> response = identityFeignClient.createIdentity(
                Map.of("roleName", roleName, "userId", userId, "role", role)
        );

        if (response.getCode() != 200) {
            logger.error("身份记录创建失败: {}", response.getMessage());
            throw new RuntimeException("注册失败，身份信息初始化异常");
        }
        logger.info("角色身份记录初始化成功: 角色={}, 用户ID={}", role, userId);
    }

    private UserAuth validAuth(String account, Long role, Long institutionId) {
        return userAuthMapper.selectAuthByAccountAndInstitution(account, role, institutionId);
    }

}
