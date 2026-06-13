package com.shiroko.support;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Description: 可重复读取请求体的包装器
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/7 下午6:09
 */
public class RepeatedlyRequestWrapper extends HttpServletRequestWrapper {
    private static final int MAX_BODY_SIZE = 1024 * 1024; // 1MB 请求体上限，防止大请求撑爆内存
    private final byte[] body;

    public RepeatedlyRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        // 限制请求体大小，防止大文件上传等场景导致 OOM
        int contentLength = request.getContentLength();
        if (contentLength > MAX_BODY_SIZE) {
            throw new IOException("Request body too large: " + contentLength + " bytes, max allowed: " + MAX_BODY_SIZE);
        }
        this.body = request.getInputStream().readAllBytes();
    }

    @Override
    public ServletInputStream getInputStream() {
        final ByteArrayInputStream bais = new ByteArrayInputStream(body);
        return new ServletInputStream() {
            @Override
            public int read() { return bais.read(); }
            @Override
            public boolean isFinished() { return false; }
            @Override
            public boolean isReady() { return true; }
            @Override
            public void setReadListener(ReadListener readListener) {}
        };
    }

    public String getBodyString() {
        return new String(body, StandardCharsets.UTF_8);
    }
}
