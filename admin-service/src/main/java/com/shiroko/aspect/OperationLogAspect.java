package com.shiroko.aspect;

import com.shiroko.annotation.OperationLog;
import com.shiroko.context.UserContext;
import com.shiroko.mapper.SysOperationLogMapper;
import com.shiroko.repository.entity.SysOperationLog;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class OperationLogAspect {

    private final SysOperationLogMapper sysOperationLogMapper;

    @Around("@annotation(operationLog)")
    public Object around(ProceedingJoinPoint point, OperationLog operationLog) throws Throwable {
        long startTime = System.currentTimeMillis();

        Object result = point.proceed();

        long duration = System.currentTimeMillis() - startTime;

        try {
            saveLog(point, operationLog, duration);
        } catch (Exception e) {
            log.warn("保存操作日志失败: {}", e.getMessage());
        }

        return result;
    }

    private void saveLog(ProceedingJoinPoint point, OperationLog operationLog, long duration) {
        MethodSignature signature = (MethodSignature) point.getSignature();

        SysOperationLog logEntity = new SysOperationLog();
        logEntity.setOperation(operationLog.value());
        logEntity.setMethod(signature.getDeclaringTypeName() + "." + signature.getName());

        // 记录请求参数（截断过长参数）
        Object[] args = point.getArgs();
        if (args != null && args.length > 0) {
            String params = args[0] != null ? args[0].toString() : "";
            if (params.length() > 2000) {
                params = params.substring(0, 2000) + "...";
            }
            logEntity.setParams(params);
        }

        // 获取当前用户信息
        Long userId = UserContext.getUserId();
        logEntity.setUserId(userId);
        logEntity.setUsername(UserContext.getUsername());

        // 获取请求IP
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String ip = request.getHeader("X-Forwarded-For");
            if (ip == null || ip.isEmpty()) {
                ip = request.getHeader("X-Real-IP");
            }
            if (ip == null || ip.isEmpty()) {
                ip = request.getRemoteAddr();
            }
            logEntity.setIp(ip);
        }

        logEntity.setDuration(duration);
        logEntity.setCreateTime(LocalDateTime.now());

        sysOperationLogMapper.insert(logEntity);
    }
}
