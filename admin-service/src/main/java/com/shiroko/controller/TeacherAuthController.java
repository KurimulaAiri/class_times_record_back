package com.shiroko.controller;

import com.shiroko.annotation.OperationLog;
import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.service.TeacherAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/teacher_auth")
@RequiredArgsConstructor
public class TeacherAuthController {

    private final TeacherAuthService teacherAuthService;

    @PostMapping("/get")
    public ResponseDTO<Map<String, Object>> getTeacherAuth(@RequestBody Map<String, Long> params) {
        return teacherAuthService.getTeacherAuth(params.get("teacherId"));
    }

    @PostMapping("/update_account")
    @OperationLog("修改教师账号")
    public ResponseDTO<String> updateTeacherAccount(@RequestBody Map<String, String> params) {
        Long teacherId = Long.parseLong(params.get("teacherId"));
        String newAccount = params.get("account");
        return teacherAuthService.updateTeacherAccount(teacherId, newAccount);
    }

    @PostMapping("/update_password")
    @OperationLog("修改教师密码")
    public ResponseDTO<String> updateTeacherPassword(@RequestBody Map<String, String> params) {
        Long teacherId = Long.parseLong(params.get("teacherId"));
        String newPassword = params.get("password");
        return teacherAuthService.updateTeacherPassword(teacherId, newPassword);
    }
}
