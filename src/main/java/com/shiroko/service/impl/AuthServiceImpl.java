package com.shiroko.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shiroko.context.UserContext;
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
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * Description: 认证服务实现类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/3/19 下午4:13
 */
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserAuthMapper userAuthMapper;
    @Value("${uni-app.wx.app-id}")
    private String appid;

    @Value("${uni-app.wx.secret}")
    private String secret;

    // 使用 @Value 注入配置，可以设置默认值以防配置缺失
    @Value("${crypto.sm2.private-key}")
    private String privateKey;

    private final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final RestTemplate restTemplate = new RestTemplate();
    private final JwtUtils jwtUtils;

    private final UserService userService;
    private final UserPlatformService userPlatformService;
    private final PermissionService permissionService;
    private final UserAuthService userAuthService;

    private final Map<String, IService<? extends RoleBaseEntity>> identityServiceMap;

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

        // 0. 参数前置校验
        if (institutionId == null || institutionId <= 0) {
            return ResponseDTO.fail("请选择或添加登录机构");
        }

        // 1. 解密前端传输密码
        String password = SM2Util.decrypt(dto.getPassword(), privateKey);

        // 2. 获取角色权限定义
        Permission permission = permissionService.getById(role);
        if (permission == null) {
            return ResponseDTO.fail("非法角色参数");
        }

        // 3. 精准定位认证信息（通过机构、账号、角色找人）
        UserAuth userAuth = validAuth(account, role, institutionId);
        if (userAuth == null) {
            return ResponseDTO.fail("该机构下不存在当前账号或身份不匹配");
        } else {
            userAuth.setLastLoginTime(LocalDateTime.now());
            userAuthService.updateById(userAuth);
        }

        // 4. 盐值密码哈希校验
        String salt = userAuth.getSalt();
        String encryptedPassword = SM3Util.digestWithSalt(password, salt);
        if (!encryptedPassword.equals(userAuth.getPassword())) {
            return ResponseDTO.fail("账号或密码错误");
        }

        // 5. 密码验证通过，获取正式的用户完整信息
        UserVO<RoleBaseEntity> user = userService.getFullUserInfoByUserId(userAuth.getUserId(), permission);
        if (user == null) {
            return ResponseDTO.fail("用户账户异常");
        }

        // 6. 校验该用户是否拥有特定角色权限（激活状态）
        if (!hasRolePermission(user.getUserId(), role)) {
            return ResponseDTO.fail("用户身份不可用（当前用户未注册该身份或该身份被禁用）");
        }

        // 7. 核心修正：后置精准校验与绑定（带着 userId 去查，绝不会查出多条）
        UserPlatform existRecord = userPlatformService.getOne(
                new LambdaQueryWrapper<UserPlatform>()
                        .eq(UserPlatform::getPlatform, platform)
                        .eq(UserPlatform::getOpenId, openId)
                        .eq(UserPlatform::getUserId, user.getUserId()) // 加上 userId 组合查询，精准定位
        );

        if (existRecord == null) {
            // 场景：此微信是合法的微信，但这是它第一次尝试登录这个【特定的老账号】，直接帮他追加绑定
            UserPlatform newPlatform = new UserPlatform();
            newPlatform.setUserId(user.getUserId());
            newPlatform.setPlatform(platform);
            newPlatform.setOpenId(openId);
            newPlatform.setIsAvailable(true); // 默认可用

            try {
                userPlatformService.save(newPlatform);
            } catch (DuplicateKeyException e) {
                // 并发异常忽略，说明别的线程并发写入了
                // 既然并发写入了，再安全复检一下状态
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
            // 场景：已经有绑定记录了，精准看当前微信对【当前这个账号】有没有被禁用
            if (!existRecord.getIsAvailable()) {
                return ResponseDTO.fail("当前微信登录权限已被禁用，请联系管理员");
            } else {
                // 更新上次登录时间和平台
                existRecord.setPlatform(platform);
                existRecord.setLastLoginTime(LocalDateTime.now());
                existRecord.setLastLoginRole(role);
                userPlatformService.updateById(existRecord);
            }
        }

        // 8. 签发双 Token
        String accessToken = jwtUtils.createAccessToken(user.getUserId(), role);
        String refreshToken = jwtUtils.createRefreshToken(user.getUserId(), role);

        return ResponseDTO.success(new LoginVO(accessToken, refreshToken, openId, user));
    }

    @Override
    public ResponseDTO<LoginVO> getOpenId(String code) {
        // 1. 拼接微信 API 地址
        String url = String.format(
                "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
                appid, secret, code
        );

        // 2. 发起请求
        String response = restTemplate.getForObject(url, String.class);
        JSONObject jsonObject = JSON.parseObject(response);

        // 3. 检查微信返回结果
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

        // 更新上次登录时间
        UserAuth userAuth = userAuthService.getOne(new LambdaQueryWrapper<UserAuth>()
                .eq(UserAuth::getUserId, userId)
                .eq(UserAuth::getRoleId, roleId)
        );
        userAuth.setLastLoginTime(LocalDateTime.now());
        userAuthService.updateById(userAuth);

        // 更新上次登录时间和平台
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
        // 1. 账号唯一性检查
        Long userRole = dto.getRole();
        String openId = dto.getOpenId();

        // 2. 检查openid是否已注册，未注册则注册
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

        // 2. 解密前端 SM2 密文得到明文
        String rawPassword = SM2Util.decrypt(dto.getPassword(), privateKey);

        // 3. 【核心步骤】自动生成随机盐值
        // 使用 UUID 去掉横杠，生成 32 位随机字符串
        String salt = UUID.randomUUID().toString().replace("-", "");

        // 4. 使用 SM3 算法结合盐值进行哈希
        // 计算公式：SM3(明文 + 盐)
        String hashedPassword = SM3Util.digestWithSalt(rawPassword, salt);

        // 5. 保存到数据库

        UserAuth userAuth = new UserAuth();
        userAuth.setUserId(userId);
        userAuth.setAccount(dto.getAccount());
        userAuth.setPassword(hashedPassword); // 存入哈希后的密文
        userAuth.setSalt(salt);               // 必须存入盐值，否则登录时无法校验
        userAuth.setRoleId(userRole);

        userAuthService.save(userAuth);

        // 6. 关联身份表 (如 Teacher/Parent)
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
     * 通用角色权限校验
     *
     * @param userId 用户ID
     * @param role   角色ID (例如: 1L)
     */
    private boolean hasRolePermission(Long userId, Long role) {
        try {
            // 1. 拼装 Service 的 Bean 名称 (遵循 Spring 默认命名规范：类名首字母小写)
            // 例如：role="teacher" -> beanName="teacherServiceImpl"
            String roleName = permissionService.getById(role).getPermissionName();
            String beanName = roleName.toLowerCase() + "Service";

            // 2. 从上下文动态获取 Service
            // 注意：所有的角色 Service 必须继承了 IService
            IService<?> roleService = identityServiceMap.get(beanName);

            // 3. 构造通用查询条件
            QueryWrapper<Object> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", userId);

            // 在 AuthServiceImpl 中调用
            return checkIdentity(roleService, userId);

        } catch (NoSuchBeanDefinitionException e) {
            logger.error("未找到对应的角色服务: {}", role);
            return false;
        }
    }

    /**
     * 这是一个泛型辅助方法，用来绑定 Service 和 Wrapper 的类型
     */
    private <T> boolean checkIdentity(IService<T> service, Long userId) {
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.eq("is_available", true);
        return service.count(wrapper) > 0;
    }

    /**
     * 通用身份记录创建（使用 Spring ReflectionUtils）
     * 逻辑：根据角色名获取 Service -> 获取实体类 -> 实例化 -> 注入 userId -> 保存
     * @param userId 用户ID
     * @param role   角色ID (例如: 1L)
     */
    private void saveIdentityRecord(Long userId, Long role) {
        // 1. 拼装 Bean 名称，例如 "teacherService"
        String roleName = permissionService.getById(role).getPermissionName();
        String beanName = roleName.toLowerCase() + "Service";

        IService<?> identityService = identityServiceMap.get(beanName);
        // 调用泛型辅助方法，将 <?> 捕获为具体的 <T>
        handleSave(identityService, userId, role, beanName);

    }

    /**
     * Description: 通用身份记录创建（使用 Spring ReflectionUtils）
     *
     * @param service  角色服务实例
     * @param userId   用户ID
     * @param role     角色ID (例如: 1L)
     * @param beanName 角色服务 Bean 名称
     * @author Guguguy
     * @since 2026/5/20 上午12:35
     */
    private <T> void handleSave(IService<T> service, Long userId, Long role, String beanName) {
        try {
            // 此时我们有了具体的 T，可以安全地通过 T 获取 Class
            Class<T> entityClass = service.getEntityClass();
            T entity = entityClass.getDeclaredConstructor().newInstance();

            Field field = ReflectionUtils.findField(entityClass, "userId");

            // 使用断言：如果为 null，直接抛出异常，并带有错误描述
            Assert.notNull(field, "实体类 " + entityClass.getSimpleName() + " 必须包含 userId 字段");


            ReflectionUtils.makeAccessible(field);
            ReflectionUtils.setField(field, entity, userId);
            // 这里的 service 是 IService<T>，entity 是 T，完美匹配！
            service.save(entity);
            logger.info("角色身份记录初始化成功: 角色={}, 用户ID={}", role, userId);

        } catch (NoSuchBeanDefinitionException e) {
            logger.error("未找到对应的角色服务 Bean: {}", beanName);
            throw new RuntimeException("非法的角色类型: " + role);
        } catch (Exception e) {
            logger.error("通用身份记录创建异常", e);
            // 必须抛出运行时异常，确保外层的 @Transactional 能够触发事务回滚
            throw new RuntimeException("注册失败，身份信息初始化异常");
        }
    }

    private UserAuth validAuth(String account, Long role, Long institutionId) {
        return userAuthMapper.selectAuthByAccountAndInstitution(account, role, institutionId);
    }

}
