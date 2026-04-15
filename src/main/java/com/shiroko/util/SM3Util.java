package com.shiroko.util;

import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

import java.nio.charset.StandardCharsets;
import java.security.Security;

/**
 * Description: SM3 工具类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/4/15 下午9:57
 */
public class SM3Util {

    static {
        // 注册算法提供者
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    /**
     * SM3 摘要计算（不带盐值）
     * @param srcData 明文
     * @return 64位十六进制字符串
     */
    public static String digest(String srcData) {
        if (srcData == null) return null;
        byte[] srcBytes = srcData.getBytes(StandardCharsets.UTF_8);
        byte[] hash = hash(srcBytes);
        return Hex.toHexString(hash);
    }

    /**
     * SM3 摘要计算（带盐值 - 推荐用于存储密码）
     * @param srcData 明文
     * @param salt 盐值（通常是随机字符串）
     * @return 64位十六进制字符串
     */
    public static String digestWithSalt(String srcData, String salt) {
        if (srcData == null || salt == null) return null;
        String combined = srcData + salt;
        return digest(combined);
    }

    /**
     * 底层哈希计算
     */
    private static byte[] hash(byte[] srcData) {
        SM3Digest digest = new SM3Digest();
        digest.update(srcData, 0, srcData.length);
        byte[] hash = new byte[digest.getDigestSize()];
        digest.doFinal(hash, 0);
        return hash;
    }

    /**
     * 校验摘要是否匹配
     * @param srcData 明文
     * @param hashData 数据库中的哈希值
     * @return 是否一致
     */
    public static boolean verify(String srcData, String hashData) {
        if (srcData == null || hashData == null) return false;
        String newHash = digest(srcData);
        return newHash.equalsIgnoreCase(hashData);
    }
}