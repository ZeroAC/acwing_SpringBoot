package com.kob.backend;

import com.kob.backend.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class BackendApplicationTests {

    @Test
    void contextLoads() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println(passwordEncoder.encode("123"));
    }

    @Test
        // 定义一个测试JWT工具类的方法
    void testJwtUtil() {
        String uuid = JwtUtil.getUuid();
        System.out.println(uuid);
        // 调用JwtUtil类的getJwtBuilder方法，创建一个新的JWT构建器。
        // 传入的参数是：主题设置为"yxc"，一般为用户唯一标识; 有效时间设置为null（表示使用默认有效期），JWT ID设置为之前生成的uuid
//        String token = JwtUtil.createJwt("yxc");
        // 调用JwtUtil类的parseJWT方法，解析刚才构建的JWT字符串（jwt.compact()得到的字符串）。
        // 解析的结果是一个Claims对象，包含了JWT的主体信息
        Claims claims = JwtUtil.parseJwt("eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI3NzcxODc3YzVkMDE0MzhmYjhkYjBiNGYxNjllYjNiMCIsInN1YiI6IjYiLCJpc3MiOiJzZyIsImlhdCI6MTcwODMzMzg5NiwiZXhwIjoxNzA4OTM4Njk2fQ.vkHxEylGwkSi1jIdKynKEfOQE1aFtu2kePn_66r_N4Q");
        // 在控制台打印出Claims对象，该对象包含了JWT的主体信息，如：发行者、主题、签发时间等
        System.out.println(claims);
        System.out.println(JwtUtil.SIGNATURE_ALGORITHM);
    }

}
