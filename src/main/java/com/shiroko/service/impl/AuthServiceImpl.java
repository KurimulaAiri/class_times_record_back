package com.shiroko.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shiroko.repository.dto.LoginDTO;
import com.shiroko.repository.dto.RegisterDTO;
import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.entity.User;
import com.shiroko.repository.entity.UserAuth;
import com.shiroko.repository.vo.LoginVO;
import com.shiroko.repository.vo.RegisterVO;
import com.shiroko.service.AuthService;
import com.shiroko.service.PermissionService;
import com.shiroko.service.UserAuthService;
import com.shiroko.service.UserService;
import com.shiroko.util.JwtUtils;
import com.shiroko.util.SM2Util;
import com.shiroko.util.SM3Util;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.util.Set;
import java.util.UUID;

/**
 * Description: TODO
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/3/19 下午4:13
 */
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    @Value("${uni-app.wx.app-id}")
    private String appid;

    @Value("${uni-app.wx.secret}")
    private String secret;

    // 使用 @Value 注入配置，可以设置默认值以防配置缺失
    @Value("${crypto.sm2.private-key:}")
    private String privateKey;

    private static final Set<Long> ALLOWED_ROLES = Set.of(1L, 2L, 3L, 4L);

    private final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final RestTemplate restTemplate;
    private final JwtUtils jwtUtils;

    private final UserService userService;
    private final PermissionService permissionService;
    private final UserAuthService userAuthService;

    private final ApplicationContext applicationContext;

    @Autowired
    private AuthServiceImpl(
            JwtUtils jwtUtils,
            UserService userService,
            PermissionService permissionService,
            UserAuthService userAuthService,
            ApplicationContext applicationContext
    ) {
        this.applicationContext = applicationContext;

        this.restTemplate = new RestTemplate();
        this.jwtUtils = jwtUtils;

        this.userService = userService;
        this.permissionService = permissionService;
        this.userAuthService = userAuthService;
    }


    @Override
    public ResponseDTO<LoginVO> wxLogin(String code) {
        // 1. 调用 getOpenId 方法获取 openid
        ResponseDTO<LoginVO> openidResponse = getOpenId(code);
        if (openidResponse.getCode() != 200) {
            return openidResponse;
        }
        String openId = openidResponse.getData().getOpenId();

        // 4. 业务逻辑：根据 openid 去数据库查用户，没有则注册，有则更新
        Long userId = userService.saveOrUpdateUser(openId);

        // 5. 生成自定义 Token (推荐 JWT)
        String token = jwtUtils.createToken(userId, 0L);

        return ResponseDTO.success(new LoginVO(token, openId));


    }

    @Override
    public ResponseDTO<LoginVO> loginByPwd(LoginDTO dto) {

        String openId = dto.getOpenId();
        Long role = dto.getRole();
        String account = dto.getAccount();
        String password = SM2Util.decrypt(dto.getPassword(), privateKey);

        User user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getOpenid, openId));

        if (user == null) {
            return ResponseDTO.fail("用户不存在");
        } else {
            if (ALLOWED_ROLES.contains(role)) {
                if (hasRolePermission(user.getId(), role)) {

                    UserAuth userAuth = userAuthService.getOne(new LambdaQueryWrapper<UserAuth>()
                            .eq(UserAuth::getUserId, user.getId())
                            .eq(UserAuth::getRoleId, role)
                    );

                    String salt = userAuth.getSalt();
                    String encryptedPassword = SM3Util.digestWithSalt(password, salt);

                    if (encryptedPassword.equals(userAuth.getPassword()) && userAuth.getAccount().equals(account)) {
                        return ResponseDTO.success(new LoginVO(jwtUtils.createToken(user.getId(), role), openId));
                    } else {
                        return ResponseDTO.fail("账号或密码错误");
                    }
                } else {
                    return ResponseDTO.fail("用户身份不可用（当前用户为注册该身份或该身份被禁用）");
                }
            } else {
                return ResponseDTO.fail("非法角色参数");
            }
        }
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
        String openid = jsonObject.getString("openid");
        if (openid == null) {
            return ResponseDTO.fail("微信登录失败：" + jsonObject.getString("errmsg"));
        }
        return ResponseDTO.success(new LoginVO(null, openid));
    }

    @Override
    @Transactional
    public ResponseDTO<RegisterVO> register(RegisterDTO dto) {



        // 1. 账号唯一性检查
        Long userRole = dto.getRole();
        String openId = dto.getOpenId();


        // 2. 检查openid是否已注册，未注册则注册
        Long userId = userService.saveOrUpdateUser(openId);

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
            String beanName = roleName.toLowerCase() + "ServiceImpl";

            // 2. 从上下文动态获取 Service
            // 注意：所有的角色 Service 必须继承了 IService
            IService<?> roleService = applicationContext.getBean(beanName, IService.class);

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
     */
    private void saveIdentityRecord(Long userId, Long role) {
        // 1. 拼装 Bean 名称，例如 "teacherServiceImpl"
        String roleName = permissionService.getById(role).getPermissionName();
        String beanName = roleName.toLowerCase() + "ServiceImpl";
        IService<?> identityService = applicationContext.getBean(beanName, IService.class);
        // 调用泛型辅助方法，将 <?> 捕获为具体的 <T>
        handleSave(identityService, userId, role, beanName);

    }

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

}
