package com.kob.backend.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.UUID;

/**
 * @author zeroac
 */ // 使用@Component标注，以便Spring容器可以自动发现并配置它
@Component
public class JwtUtil {
    // 每当有重复的代码部分并且不依赖于其容器类的状态时，它们就可以集中在“实用程序类”内。
    // 实用程序类是仅具有静态成员的类，因此不应实例化它。
    private JwtUtil() {
    }

    // JWT的密钥，用于签名和验证JWT
    private static final String JWT_SECRET = "SDFGjhdsfalshdfHFdsjkdsfds121232131afasdfac";
    // JWT的有效期为7天，即7天后token失效
    private static final long JWT_TTL = 1000L * 60 * 60 * 24 * 7;
    // 使用HS256算法签名JWT
    public static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;
    // 秘钥生成
    private static final SecretKey SECRET_KEY = generalKey();

    // 生成UUID，用于JWT的ID字段，通常是一个随机生成的字符串
    public static String getUUID() {
        // 使用 UUID 类的 randomUUID 方法生成一个随机 UUID 对象，然后将其转换为字符串
        // 使用字符串的 replaceAll 方法去除字符串中的所有连字符（-）
        return UUID.randomUUID().toString().replace("-", "");
    }

    // 从JWT_KEY解码获取SecretKey对象，用于签名和验证JWT
    public static SecretKey generalKey() {
        byte[] encodedKey = JWT_SECRET.getBytes();
        return new SecretKeySpec(encodedKey, 0, encodedKey.length, "HmacSHA256");
    }

    // 构建JWT的方法，包括设置签发者、主题、签发时间、过期时间等信息
    public static JwtBuilder getJwtBuilder(String subject, Long ttlMillis, String uuid) {
        //获取当前时间戳
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        // 如果未设置自定义有效期，则使用默认有效期
        if (ttlMillis == null) {
            ttlMillis = JwtUtil.JWT_TTL;
        }
        // 计算JWT的过期时间
        long expMillis = nowMillis + ttlMillis;
        Date expDate = new Date(expMillis);
        // 构建JWT
        return Jwts.builder()
                .setId(uuid) // 设置JWT ID 为了防止重放攻击
                .setSubject(subject) // 设置主题
                .setIssuer("sg") // 设置签发者
                .setIssuedAt(now) // 设置签发时间
                .signWith(SECRET_KEY, SIGNATURE_ALGORITHM) // 签名JWT
                .setExpiration(expDate); // 设置过期时间

    }

    // 解析JWT的方法，从JWT字符串中解析出Claims对象，包含了JWT的主体信息
    public static Claims parseJwt(String jwt) {
        // 解析JWT，如果JWT被篡改或者过期，会抛出异常
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY) // 设置解析时使用的签名密钥
                .build()
                .parseClaimsJws(jwt) // 解析JWT
                .getBody(); // 获取JWT的payload部分
    }
}
