package com.kob.backend.service.account.impl;

import com.kob.backend.dao.UserDao;
import com.kob.backend.pojo.User;
import com.kob.backend.service.account.RegisterService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zeroac
 */
@Service
@RequiredArgsConstructor
public class RegisterServiceImpl implements RegisterService {
    private static final Logger logger = LoggerFactory.getLogger(RegisterServiceImpl.class);
    private final UserDao userDao;

    private final PasswordEncoder passwordEncoder;

    public LocalDateTime getNowTime() {
        return LocalDateTime.now();
    }

    @Override
    public Map<String, String> register(String username, String password, String confirmedPassword) {
        Map<String, String> map = new HashMap<>();
        if (username == null || password == null || confirmedPassword == null) {
            map.put("error_message", "注册参数不能为空");
            return map;
        }
        username = username.trim();
        password = password.trim();
        confirmedPassword = confirmedPassword.trim();
        if (username.isEmpty() || password.isEmpty() || confirmedPassword.isEmpty()) {
            map.put("error_message", "注册参数不能为空");
            return map;
        }
        if (!password.equals(confirmedPassword)) {
            map.put("error_message", "两次密码不一致");
            return map;
        }
        if (username.length() > 100) {
            map.put("error_message", "用户名长度不能大于100");
            return map;
        }
        if (password.length() < 6 || password.length() > 20) {
            map.put("error_message", "密码长度应该在6到20位");
            return map;
        }
        if (userDao.selectByUsername(username) != null) {
            map.put("error_message", "用户名已存在");
            return map;
        }
        String encodedPassword = passwordEncoder.encode(password);
        String photo = "https://cdn.acwing.com/media/user/profile/photo/1911_lg_0cc850c637.jpg";//默认头像
        User user = new User(null, username, encodedPassword, photo, getNowTime(), getNowTime(), null, false);
        userDao.insert(user);
        map.put("error_message", "success");
        return map;
    }
}
