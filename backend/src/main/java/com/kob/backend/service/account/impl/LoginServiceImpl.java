package com.kob.backend.service.account.impl;

import com.kob.backend.pojo.User;
import com.kob.backend.service.account.LoginService;
import com.kob.backend.service.account.model.UserDetailsImpl;
import com.kob.backend.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor // 自动为所有final字段生成构造函数
public class LoginServiceImpl implements LoginService {

    private final AuthenticationManager authenticationManager;

    @Override
    public Map<String, String> getToken(String username, String password) {
        Map<String, String> map = new HashMap<>();

        try {
            // 使用用户名和密码创建一个AuthenticationToken对象
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            // 对AuthenticationToken对象进行验证，如果成功，返回一个包含用户详细信息的Authentication对象
            Authentication authenticate = authenticationManager.authenticate(authenticationToken);
            // 从Authentication对象中获取用户详细信息
            UserDetailsImpl loginUser = (UserDetailsImpl) authenticate.getPrincipal();
            // 从用户详细信息中获取User对象
            User user = loginUser.getUser();
            // 使用User对象的id生成JWT令牌
            String jwt = JwtUtil.createJWT(user.getId().toString());

            // 在Map中添加一个表示操作成功的消息
            map.put("error_message", "success");
            // 在Map中添加生成的JWT令牌
            map.put("token", jwt);
        } catch (AuthenticationException e) {
            map.put("error_message", "Authentication failed: " + e.getMessage());
            // Depending on your application's requirement, you might want to log this exception or handle it differently.
        } catch (Exception e) {
            map.put("error_message", "An unexpected error occurred: " + e.getMessage());
            // This is a generic Exception handler. You might want to catch more specific exceptions and handle them accordingly.
        }

        return map;
    }
}
