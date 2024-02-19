package com.kob.backend.config.filter;

import com.kob.backend.config.exception.TokenParsingException;
import com.kob.backend.config.exception.UserNotLoggedInException;
import com.kob.backend.dao.UserDao;
import com.kob.backend.pojo.User;
import com.kob.backend.service.account.model.UserDetailsImpl;
import com.kob.backend.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author zeroac
 */
@Component
@RequiredArgsConstructor // 自动为所有final字段生成构造函数
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    private final UserDao userDao;

    /**
     * 该方法为自定义的Spring Security过滤器中的核心方法，用于拦截请求并执行JWT认证。
     * 如果请求中包含有效的JWT令牌，则用户身份将被认证并允许请求继续执行。
     * 如果JWT令牌无效，则会抛出异常。
     *
     * @param request     当前请求对象
     * @param response    当前响应对象
     * @param filterChain 过滤器链
     * @throws ServletException 如果发生过滤器内部错误
     * @throws IOException      如果发生I/O错误
     * @throws RuntimeException 如果发生运行时异常
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, RuntimeException {
        // 从请求头中获取名为"token"的JWT令牌
        String token = request.getHeader("Authorization");
        System.out.println("拦截器，获取token: " + token);
        // 如果令牌为空或者格式不正确（不是以"Bearer "开头），则直接放行请求 转到账号密码过滤器验证
        if (!StringUtils.hasText(token) || !token.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        // 去掉令牌前缀"Bearer "，获取真正的令牌字符串
        token = token.substring(7);
        String userId;
        try {
            // 解析JWT令牌，获取其中的声明（Claims）
            Claims claims = JwtUtil.parseJwt(token);
            // 从声明中获取用户ID
            userId = claims.getSubject();
        } catch (Exception e) {
            // 如果解析JWT令牌失败，抛出自定义的TokenParsingException异常
            throw new TokenParsingException("token解析出错", e);
        }
        // 根据用户ID查询用户信息
        User user = userDao.selectById(Integer.parseInt(userId));
        if (user == null) {
            throw new UserNotLoggedInException("用户未登录");
        }
        // 创建UserDetails对象，用于存放已认证用户的信息
        UserDetailsImpl loginUser = new UserDetailsImpl(user);
        // 创建UsernamePasswordAuthenticationToken对象，用于在Spring Security上下文中设置用户认证信息
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginUser, null, loginUser.getAuthorities()
        );
        // 在Spring Security上下文中设置authenticationToken，完成认证过程
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        // 认证完成后，继续执行后续的过滤器链
        filterChain.doFilter(request, response);
    }
}
