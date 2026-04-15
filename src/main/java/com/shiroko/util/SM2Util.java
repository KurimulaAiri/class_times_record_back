package com.shiroko.util;

import org.bouncycastle.asn1.gm.GMNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.util.encoders.Hex;

import java.math.BigInteger;

/**
 * Description: SM2 工具类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/4/15 下午4:38
 */
public class SM2Util {

    /**
     * SM2 解密
     * @param cipherTextHex 前端传来的加密十六进制字符串
     * @param privateKeyHex 后端保存的私钥十六进制字符串
     * @return 解密后的明文
     */
    public static String decrypt(String cipherTextHex, String privateKeyHex) {
        try {
            // 1. 获取 SM2 曲线参数
            X9ECParameters x9ECParameters = GMNamedCurves.getByName("sm2p256v1");
            SM2Engine engine = getSm2Engine(privateKeyHex, x9ECParameters);

            // 5. 解密
            byte[] cipherData = Hex.decode(cipherTextHex);
            byte[] result = engine.processBlock(cipherData, 0, cipherData.length);

            return new String(result);
        } catch (Exception e) {
            throw new RuntimeException("SM2解密失败: " + e.getMessage());
        }
    }

    private static SM2Engine getSm2Engine(String privateKeyHex, X9ECParameters x9ECParameters) {
        ECDomainParameters domainParameters = new ECDomainParameters(
                x9ECParameters.getCurve(), x9ECParameters.getG(), x9ECParameters.getN(), x9ECParameters.getH());

        // 2. 将 Hex 私钥还原为大整数 D，并构建私钥参数对象
        BigInteger d = new BigInteger(privateKeyHex, 16);
        ECPrivateKeyParameters privateKeyParams = new ECPrivateKeyParameters(d, domainParameters);

        // 3. 初始化 SM2 引擎
        // 注意：Mode.C1C3C2 是新国标，sm-crypto 库默认通常也是这个。如果报错，请尝试 Mode.C1C2C3
        SM2Engine engine = new SM2Engine(SM2Engine.Mode.C1C3C2);

        // 4. 设置为解密模式
        engine.init(false, privateKeyParams);
        return engine;
    }
}