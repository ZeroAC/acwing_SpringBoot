package com.kob.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Spring Security的配置类。
 * 使用@Configuration注解标记这个类为配置类，表示它主要用于配置Bean。
 *
 * @author zeroac
 * @EnableWebSecurity注解开启了Spring Security的Web安全功能。
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * 定义一个Bean来提供密码的编码功能。
     * 这里使用了BCryptPasswordEncoder，它是基于BCrypt强散列方法的密码编码器。
     * BCrypt是一种根据Blowfish密码学算法实现的自适应散列函数，可以防止彩虹表攻击，
     * 并且可以通过增加迭代次数来适应硬件性能的提升。
     *
     * @return 返回一个使用BCrypt散列算法的PasswordEncoder实例。
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}