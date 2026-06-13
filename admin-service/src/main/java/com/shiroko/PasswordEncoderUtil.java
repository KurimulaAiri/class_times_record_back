package com.shiroko;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 密码编码工具，用于生成 BCrypt 密码哈希。
 * <p>
 * 运行方式：
 *   mvn -pl admin-service exec:java -Dexec.mainClass="com.shiroko.PasswordEncoderUtil" -Dexec.args="admin123"
 *   或不带参数进入交互模式：
 *   mvn -pl admin-service exec:java -Dexec.mainClass="com.shiroko.PasswordEncoderUtil"
 */
public class PasswordEncoderUtil {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if (args.length > 0) {
            for (String raw : args) {
                System.out.println(raw + " -> " + encoder.encode(raw));
            }
        } else {
            System.out.println("=== BCrypt Password Encoder ===");
            System.out.println("输入明文密码（输入 quit 退出）：");

            java.util.Scanner scanner = new java.util.Scanner(System.in);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if ("quit".equalsIgnoreCase(line) || "exit".equalsIgnoreCase(line)) {
                    break;
                }
                if (line.isEmpty()) {
                    continue;
                }
                String encoded = encoder.encode(line);
                System.out.println("明文: " + line);
                System.out.println("哈希: " + encoded);
                System.out.println("验证: " + encoder.matches(line, encoded));
                System.out.println();
            }
            scanner.close();
        }
    }
}
