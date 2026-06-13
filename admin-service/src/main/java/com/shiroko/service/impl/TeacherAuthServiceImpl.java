package com.shiroko.service.impl;

import com.shiroko.mapper.UserAuthMapper;
import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.entity.UserAuth;
import com.shiroko.service.TeacherAuthService;
import com.shiroko.util.SM2Util;
import com.shiroko.util.SM3Util;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeacherAuthServiceImpl implements TeacherAuthService {

    private final UserAuthMapper userAuthMapper;

    @Value("${crypto.sm2.private-key}")
    private String sm2PrivateKey;

    @Override
    public ResponseDTO<Map<String, Object>> getTeacherAuth(Long teacherId) {
        UserAuth userAuth = userAuthMapper.selectAuthByTeacherId(teacherId);
        if (userAuth == null) {
            return ResponseDTO.fail("未找到该教师的账号信息");
        }
        Map<String, Object> data = new HashMap<>();
        data.put("id", userAuth.getId());
        data.put("userId", userAuth.getUserId());
        data.put("account", userAuth.getAccount());
        data.put("lastLoginTime", userAuth.getLastLoginTime());
        return ResponseDTO.success(data);
    }

    @Override
    public ResponseDTO<String> updateTeacherAccount(Long teacherId, String newAccount) {
        UserAuth userAuth = userAuthMapper.selectAuthByTeacherId(teacherId);
        if (userAuth == null) {
            return ResponseDTO.fail("未找到该教师的账号信息");
        }
        userAuth.setAccount(newAccount);
        userAuthMapper.updateById(userAuth);
        return ResponseDTO.success("账号修改成功");
    }

    @Override
    public ResponseDTO<String> updateTeacherPassword(Long teacherId, String newPassword) {
        UserAuth userAuth = userAuthMapper.selectAuthByTeacherId(teacherId);
        if (userAuth == null) {
            return ResponseDTO.fail("未找到该教师的账号信息");
        }
        // SM2 解密前端传来的新密码
        String plainPassword = SM2Util.decrypt(newPassword, sm2PrivateKey);
        String salt = UUID.randomUUID().toString().replace("-", "");
        userAuth.setPassword(SM3Util.digestWithSalt(plainPassword, salt));
        userAuth.setSalt(salt);
        userAuthMapper.updateById(userAuth);
        return ResponseDTO.success("密码修改成功");
    }
}
