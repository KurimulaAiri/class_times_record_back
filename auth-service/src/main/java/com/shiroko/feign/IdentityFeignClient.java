package com.shiroko.feign;

import com.shiroko.repository.dto.ResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * Feign Client：调用 business-service 的身份查询内部接口
 */
@FeignClient(name = "business-service", path = "/internal/identity")
public interface IdentityFeignClient {

    @PostMapping("/get_by_user_id")
    ResponseDTO<Map<String, Object>> getByUserId(@RequestBody Map<String, Object> params);

    @PostMapping("/check_available")
    ResponseDTO<Boolean> checkAvailable(@RequestBody Map<String, Object> params);

    @PostMapping("/create")
    ResponseDTO<String> createIdentity(@RequestBody Map<String, Object> params);
}
