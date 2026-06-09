package com.shiroko.util;

import org.bouncycastle.asn1.gm.GMNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECKeyGenerationParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

import java.security.SecureRandom;
import java.security.Security;

/**
 * Description: SM2 密钥生成器
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/4/15 下午4:32
 */
public class SM2KeyGenerator {

    static {
        // 注册 BouncyCastle 算法提供者
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    public static void generateSm2KeyPair() {
        // 1. 获取 SM2 椭圆曲线参数
        X9ECParameters sm2Params = GMNamedCurves.getByName("sm2p256v1");
        ECDomainParameters domainParameters = new ECDomainParameters(
                sm2Params.getCurve(), sm2Params.getG(), sm2Params.getN(), sm2Params.getH());

        // 2. 初始化密钥生成器
        ECKeyPairGenerator generator = new ECKeyPairGenerator();
        ECKeyGenerationParameters keyGenParams = new ECKeyGenerationParameters(domainParameters, new SecureRandom());
        generator.init(keyGenParams);

        // 3. 生成密钥对
        AsymmetricCipherKeyPair keyPair = generator.generateKeyPair();
        ECPublicKeyParameters publicKey = (ECPublicKeyParameters) keyPair.getPublic();
        ECPrivateKeyParameters privateKey = (ECPrivateKeyParameters) keyPair.getPrivate();

        // 4. 提取公钥（Hex格式）
        // getQ().getEncoded(false) 表示不压缩，通常前端 sm-crypto 库需要 04 开头的 130位 Hex
        String publicKeyHex = Hex.toHexString(publicKey.getQ().getEncoded(false));

        // 5. 提取私钥（Hex格式）
        String privateKeyHex = Hex.toHexString(privateKey.getD().toByteArray());

        System.out.println("----- SM2 密钥对生成成功 -----");
        System.out.println("公钥 (交给前端加密): " + publicKeyHex);
        System.out.println("私钥 (后端自行保存): " + privateKeyHex);
    }

    public static void main(String[] args) {
        generateSm2KeyPair();
    }
}