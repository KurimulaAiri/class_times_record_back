package com.shiroko.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.dto.auth.LoginDTO;
import com.shiroko.repository.dto.auth.RegisterDTO;
import com.shiroko.repository.vo.auth.LoginVO;
import com.shiroko.repository.vo.auth.RegisterVO;
import com.shiroko.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthControllerApiTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Mock
    private AuthService authService;
    @InjectMocks
    private AuthController authController;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    @DisplayName("POST /auth/login_no_pwd - 寰俊鍏嶅瘑鐧诲綍鎴愬姛")
    void loginNoPwd_shouldReturnToken() throws Exception {
        LoginDTO dto = new LoginDTO();
        dto.setCode("test_wx_code_001");
        dto.setRole(2L);

        LoginVO loginVO = new LoginVO();
        loginVO.setAccessToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.test.payload");
        loginVO.setRefreshToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.refresh.payload");

        when(authService.wxLogin(any(LoginDTO.class)))
                .thenReturn(ResponseDTO.success(loginVO));

        mockMvc.perform(post("/auth/login_no_pwd")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.accessToken").exists())
                .andExpect(jsonPath("$.data.refreshToken").exists());

        verify(authService).wxLogin(any(LoginDTO.class));
    }

    @Test
    @DisplayName("POST /auth/login_no_pwd - 缂哄皯code杩斿洖400")
    void loginNoPwd_missingCode_shouldReturn400() throws Exception {
        LoginDTO dto = new LoginDTO();

        mockMvc.perform(post("/auth/login_no_pwd")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());

        verify(authService, never()).wxLogin(any());
    }

    @Test
    @DisplayName("POST /auth/login_by_pwd - 瀵嗙爜鐧诲綍鎴愬姛")
    void loginByPwd_shouldReturnToken() throws Exception {
        LoginDTO dto = new LoginDTO();
        dto.setOpenId("test_open_id");
        dto.setRole(1L);
        dto.setAccount("admin");
        dto.setPassword("encrypted_password");
        dto.setNeedValidateAdmin(true);

        LoginVO loginVO = new LoginVO();
        loginVO.setAccessToken("eyJhbGciOiJIUzI1NiJ9.test_pwd.payload");
        loginVO.setRefreshToken("eyJhbGciOiJIUzI1NiJ9.refresh_pwd.payload");

        when(authService.loginByPwd(any(LoginDTO.class)))
                .thenReturn(ResponseDTO.success(loginVO));

        mockMvc.perform(post("/auth/login_by_pwd")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.accessToken").exists())
                .andExpect(jsonPath("$.data.refreshToken").exists());

        verify(authService).loginByPwd(any(LoginDTO.class));
    }

    @Test
    @DisplayName("POST /auth/login_by_pwd - 缂哄皯蹇呰瀛楁杩斿洖400")
    void loginByPwd_missingFields_shouldReturn400() throws Exception {
        LoginDTO dto = new LoginDTO();

        mockMvc.perform(post("/auth/login_by_pwd")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /auth/login_by_token - Token鐧诲綍鎴愬姛")
    void loginByToken_shouldReturnSuccess() throws Exception {
        LoginDTO dto = new LoginDTO();
        dto.setOpenId("test_open_id");
        dto.setToken("existing_token");
        dto.setNeedValidateAdmin(false);

        LoginVO loginVO = new LoginVO();
        loginVO.setAccessToken("refreshed_access_token");
        loginVO.setRefreshToken("refreshed_refresh_token");

        when(authService.loginByToken(any(LoginDTO.class)))
                .thenReturn(ResponseDTO.success(loginVO));

        mockMvc.perform(post("/auth/login_by_token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.accessToken").value("refreshed_access_token"))
                .andExpect(jsonPath("$.data.refreshToken").value("refreshed_refresh_token"));

        verify(authService).loginByToken(any(LoginDTO.class));
    }

    @Test
    @DisplayName("POST /auth/register - 娉ㄥ唽鎴愬姛")
    void register_shouldReturn200() throws Exception {
        RegisterDTO dto = new RegisterDTO();
        dto.setAccount("test_user");
        dto.setPassword("encrypted_pwd");
        dto.setRole(2L);
        dto.setOpenId("wx_open_id_001");

        RegisterVO registerVO = new RegisterVO();
        registerVO.setOpenId("wx_open_id_001");

        when(authService.register(any(RegisterDTO.class)))
                .thenReturn(ResponseDTO.success(registerVO));

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.openId").value("wx_open_id_001"));

        verify(authService).register(any(RegisterDTO.class));
    }

    @Test
    @DisplayName("POST /auth/register - 缂哄皯蹇呭～瀛楁杩斿洖400")
    void register_missingFields_shouldReturn400() throws Exception {
        RegisterDTO dto = new RegisterDTO();

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());

        verify(authService, never()).register(any());
    }

    @Test
    @DisplayName("POST /auth/logout - 閫€鍑虹櫥褰曟垚鍔?)
    void logout_shouldReturn200() throws Exception {
        LoginDTO dto = new LoginDTO();
        dto.setToken("token_to_logout");

        when(authService.logout(any(LoginDTO.class)))
                .thenReturn(ResponseDTO.success("閫€鍑烘垚鍔?));

        mockMvc.perform(post("/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        verify(authService).logout(any(LoginDTO.class));
    }

    @Test
    @DisplayName("POST /auth/get_open_id - 鑾峰彇OpenId鎴愬姛")
    void getOpenId_shouldReturn200() throws Exception {
        LoginDTO dto = new LoginDTO();
        dto.setCode("wx_code_001");

        LoginVO loginVO = new LoginVO();
        loginVO.setOpenId("wx_open_id_from_server");

        when(authService.getOpenId(anyString()))
                .thenReturn(ResponseDTO.success(loginVO));

        mockMvc.perform(post("/auth/get_open_id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.openId").value("wx_open_id_from_server"));

        verify(authService).getOpenId(anyString());
    }

    @Test
    @DisplayName("POST /auth/refresh - 鍒锋柊AccessToken鎴愬姛")
    void refreshAccessToken_shouldReturnNewAccessToken() throws Exception {
        LoginDTO dto = new LoginDTO();
        dto.setToken("valid_refresh_token");

        LoginVO loginVO = new LoginVO();
        loginVO.setAccessToken("new_access_token_value");

        when(authService.refreshAccessToken(anyString()))
                .thenReturn(ResponseDTO.success(loginVO));

        mockMvc.perform(post("/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.accessToken").value("new_access_token_value"));

        verify(authService).refreshAccessToken(anyString());
    }
}
