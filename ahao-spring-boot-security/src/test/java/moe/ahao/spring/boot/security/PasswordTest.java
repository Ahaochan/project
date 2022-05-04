package moe.ahao.spring.boot.security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

public class PasswordTest {
    public static final String PW = "123456";

    @Test
    public void BCrypt() {
        valid(new BCryptPasswordEncoder(16));
    }

    @Test
    public void Argon2() {
        try {
            Class.forName("org.bouncycastle.crypto.params.Argon2Parameters$Builder");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            // TODO 依赖未加载
            Assumptions.assumeTrue(false, "依赖未加载");
        }

        valid(new Argon2PasswordEncoder()); // 依赖 org.bouncycastle.crypto.params.Argon2Parameters$Builder
    }

    @Test
    public void Pbkdf2() {
        valid(new Pbkdf2PasswordEncoder());
    }

    @Test
    public void SCrypt() {
        // TODO NoClassDefFoundError: org/bouncycastle/crypto/generators/SCrypt
        valid(new SCryptPasswordEncoder());
    }

    private void valid(PasswordEncoder encoder) {
        String result = encoder.encode(PW);
        System.out.println("加密前:" + PW);
        System.out.println("加密后:" + result);
        Assertions.assertTrue(encoder.matches(PW, result));
    }


}
