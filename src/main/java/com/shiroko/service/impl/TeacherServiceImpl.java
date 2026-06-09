package com.shiroko.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiroko.converter.TeacherConverter;
import com.shiroko.mapper.TeacherMapper;
import com.shiroko.mapper.UserAuthMapper;
import com.shiroko.mapper.UserMapper;
import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.dto.teacher.InsertTeacherDTO;
import com.shiroko.repository.dto.teacher.QueryTeacherDTO;
import com.shiroko.repository.dto.teacher.UpdateTeacherDTO;
import com.shiroko.repository.entity.Teacher;
import com.shiroko.repository.entity.User;
import com.shiroko.repository.entity.UserAuth;
import com.shiroko.repository.vo.teacher.InsertTeacherVO;
import com.shiroko.repository.vo.teacher.QueryTeacherVO;
import com.shiroko.repository.vo.teacher.TeacherVO;
import com.shiroko.repository.vo.teacher.UpdateTeacherVO;
import com.shiroko.service.TeacherService;
import com.shiroko.service.UserAuthService;
import com.shiroko.util.SM2Util;
import com.shiroko.util.SM3Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Description: 教师服务实现
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/4/15 下午22:37
 */
@Service("teacherService")
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
@Slf4j
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService{

    private final UserMapper userMapper;
    private final TeacherConverter teacherConverter;
    private final UserAuthService userAuthService;

    private final TeacherMapper teacherMapper;
    private final UserAuthMapper userAuthMapper;
    @Value("${crypto.sm2.private-key}")
    private String privateKey;

    @Override
    public ResponseDTO<QueryTeacherVO> getTeacherById(QueryTeacherDTO queryTeacherDTO) {

        TeacherVO teacherVO = teacherMapper.getTeacherById(queryTeacherDTO.getTeacherId());

        QueryTeacherVO queryTeacherVO = new QueryTeacherVO();
        List<TeacherVO> teachers = new ArrayList<>();
        teachers.add(teacherVO);
        queryTeacherVO.setTeachers(teachers);
        queryTeacherVO.setTotal(1L);

        return ResponseDTO.success(queryTeacherVO);
    }

    @Override
    public ResponseDTO<QueryTeacherVO> getTeacherByInstitutionId(QueryTeacherDTO queryTeacherDTO) {
        List<TeacherVO> teachers = teacherMapper.getTeacherByInstitutionId(queryTeacherDTO.getInstitutionId());

        QueryTeacherVO queryTeacherVO = new QueryTeacherVO();
        queryTeacherVO.setTeachers(teachers);
        queryTeacherVO.setTotal((long) teachers.size());

        return ResponseDTO.success(queryTeacherVO);
    }

    @Override
    public ResponseDTO<UpdateTeacherVO> updateTeacherById(UpdateTeacherDTO updateTeacherDTO) {
        int effect = this.teacherMapper.updateById(teacherConverter.updateDTOToPOJO(updateTeacherDTO));

        if (updateTeacherDTO.getPassword() != null || updateTeacherDTO.getAccount() != null) {
            UserAuth userAuth = userAuthMapper.selectAuthByTeacherId(updateTeacherDTO.getTeacherId());
            if (userAuth != null) {
                // 更新账号
                if (updateTeacherDTO.getAccount() != null) {
                    log.debug("更新账号: {}", updateTeacherDTO.getAccount());
                    // 【新增核心步骤】唯一性校验
                    Long institutionId = updateTeacherDTO.getInstitutionId();
                    String account = updateTeacherDTO.getAccount();
                    Long roleId = 4L; // 教师角色ID

                    boolean isDuplicate = userAuthMapper.existsByInstitutionAndAccountAndRole(institutionId, account, roleId);
                    if (isDuplicate) {
                        log.warn("该机构 {} 下已存在相同的账号: {}", institutionId, account);
                        return ResponseDTO.fail("该机构下已存在相同的账号");
                    }
                    userAuth.setAccount(updateTeacherDTO.getAccount());
                }
                // 更新密码
                if (updateTeacherDTO.getPassword() != null) {
                    String rawPassword = SM2Util.decrypt(updateTeacherDTO.getPassword(), privateKey);
                    String salt = UUID.randomUUID().toString().replace("-", "");
                    String hashedPassword = SM3Util.digestWithSalt(rawPassword, salt);
                    userAuth.setPassword(hashedPassword);
                    userAuth.setSalt(salt);
                }
                userAuthService.updateById(userAuth);
            }
        }

        if (effect > 0) {
            return ResponseDTO.success(new UpdateTeacherVO(effect));
        }
        return ResponseDTO.fail("更新失败");
    }

    @Override
    public ResponseDTO<InsertTeacherVO> insertTeacher(InsertTeacherDTO insertTeacherDTO) {
        // 【新增核心步骤】唯一性校验
        Long institutionId = insertTeacherDTO.getInstitutionId();
        String account = insertTeacherDTO.getAccount();
        Long roleId = 4L; // 教师角色ID

        boolean isDuplicate = userAuthMapper.existsByInstitutionAndAccountAndRole(institutionId, account, roleId);
        if (isDuplicate) {
            return ResponseDTO.fail("该机构下已存在相同的账号");
        }

        User user = new User().setInstitutionId(insertTeacherDTO.getInstitutionId());
        userMapper.insert(user);

        Teacher teacher = teacherConverter.insertDTOToPOJO(insertTeacherDTO);
        teacher.setUserId(user.getId());
        teacherMapper.insert(teacher);

        // 2. 解密前端 SM2 密文得到明文
        String rawPassword = SM2Util.decrypt(insertTeacherDTO.getPassword(), privateKey);

        // 3. 【核心步骤】自动生成随机盐值
        // 使用 UUID 去掉横杠，生成 32 位随机字符串
        String salt = UUID.randomUUID().toString().replace("-", "");

        // 4. 使用 SM3 算法结合盐值进行哈希
        // 计算公式：SM3(明文 + 盐)
        String hashedPassword = SM3Util.digestWithSalt(rawPassword, salt);

        // 5. 保存到数据库

        UserAuth userAuth = new UserAuth();
        userAuth.setUserId(user.getId());
        userAuth.setAccount(insertTeacherDTO.getAccount());
        userAuth.setPassword(hashedPassword); // 存入哈希后的密文
        userAuth.setSalt(salt);               // 必须存入盐值，否则登录时无法校验
        userAuth.setRoleId(4L);

        userAuthService.save(userAuth);

        if (userAuth.getId() != null) {
            return ResponseDTO.success(new InsertTeacherVO(teacherMapper.getTeacherById(teacher.getTeacherId())));
        }

        return ResponseDTO.fail("注册失败");
    }

}




