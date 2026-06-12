package com.shiroko.controller;

import com.shiroko.repository.dto.ResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/crypto")
public class CryptoController {

    @Value("${crypto.sm2.public-key}")
    private String sm2PublicKey;

    @GetMapping("/public_key")
    public ResponseDTO<Map<String, String>> getPublicKey() {
        return ResponseDTO.success(Map.of("publicKey", sm2PublicKey));
    }
}
