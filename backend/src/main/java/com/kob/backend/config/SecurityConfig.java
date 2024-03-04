package com.kob.backend.config;

import com.kob.backend.config.filter.JwtAuthenticationTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security的配置类。
 * 使用@Configuration注解标记这个类为配置类，表示它主要用于配置Bean。
 *
 * @author zeroac
 * @EnableWebSecurity注解开启了Spring Security的Web安全功能。
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor // 自动为所有final字段生成构造函数
public class SecurityConfig {

    // 注入自定义的JwtAuthenticationTokenFilter
    private final JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    // 注入PasswordEncoder：首先，你需要一个PasswordEncoder的实例。
    // 在Spring Security 5中，推荐的做法是使用BCryptPasswordEncoder，
    // 但也可以使用其他实现。通常，你会在配置类中创建这个bean，并将其注入到
    // 需要使用它的服务或组件中
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 定义SecurityFilterChain Bean来配置HttpSecurity
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 禁用CSRF
                .csrf(AbstractHttpConfigurer::disable)
                // 设置会话管理策略为无状态
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 配置请求权限
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                // 允许所有用户访问/token/和/register/路径
                                .antMatchers("/user/account/token", "/user/account/register").permitAll()
                                // 允许所有用户对所有OPTIONS请求
                                .antMatchers(HttpMethod.OPTIONS).permitAll()
                                .antMatchers("/websocket/**").permitAll()
                                // 任何请求都需要认证
                                .anyRequest().authenticated())
                // 添加自定义JWT过滤器
                //addFilterBefore 只是定义了过滤器在过滤链中的顺序，具体哪个过滤器会执行取决于请求的类型。
                // 对于需要JWT验证的请求，只会执行 jwtAuthenticationTokenFilter；
                // 对于需要表单登录的请求，则会执行 UsernamePasswordAuthenticationFilter。
                .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        // 利用AuthenticationConfiguration提供的getAuthenticationManager方法
        // 将AuthenticationManager配置为Spring容器中的Bean。
        // 这样做之后，我们就可以在应用程序的其他部分通过自动装配（@Autowired）机制来使用AuthenticationManager，
        // 从而完成例如用户登陆或令牌验证等认证操作。
        return authenticationConfiguration.getAuthenticationManager();
    }
}