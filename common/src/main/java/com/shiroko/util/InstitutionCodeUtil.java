package com.shiroko.util;


import org.hashids.Hashids;

/**
 * Description: 学校编码工具类：用于生成和解析学校编码的工具方法
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/27 下午10:40
 */
public class InstitutionCodeUtil {
    // 自定义的盐（密钥），只要这串盐不变，同一个 ID 算出来的机构码永远固定且唯一
    private static final String SALT = "Shiroko_Course_System_2026";
    // 期望的机构码最小长度
    private static final int MIN_CODE_LENGTH = 10;
    // 允许生成的字符集（去除了易混淆字符）
    private static final String ALPHABET = "23456789ABCDEFGHJKLMNPQRSTUVWXYZ";

    private static final Hashids hashids = new Hashids(SALT, MIN_CODE_LENGTH, ALPHABET);

    public static void main(String[] args) {
        InstitutionCodeUtil codeUtil = new InstitutionCodeUtil();
        System.out.println(codeUtil.encodeToCode((3L)));
        System.out.println(codeUtil.decodeToId("DP9YVWJ75M"));
    }

    /**
     * 根据数据库自增 ID 编码成唯一的机构码
     */
    public String encodeToCode(Long institutionId) {
        return hashids.encode(institutionId);
    }

    /**
     * 将用户输入的机构码，还原为数据库的真实自增 ID
     */
    public Long decodeToId(String code) {
        long[] ids = hashids.decode(code);
        if (ids != null && ids.length > 0) {
            return ids[0];
        }
        return null; // 解析失败，说明机构码是瞎编的
    }
}
