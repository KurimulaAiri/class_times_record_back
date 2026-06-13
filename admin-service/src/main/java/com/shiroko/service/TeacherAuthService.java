package com.shiroko.service;

import com.shiroko.repository.dto.ResponseDTO;
import java.util.Map;

public interface TeacherAuthService {
    ResponseDTO<Map<String, Object>> getTeacherAuth(Long teacherId);
    ResponseDTO<String> updateTeacherAccount(Long teacherId, String newAccount);
    ResponseDTO<String> updateTeacherPassword(Long teacherId, String newPassword);
}
