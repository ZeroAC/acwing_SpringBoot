package com.kob.backend.controller.user.account;

import com.kob.backend.controller.commons.BasisControl;
import com.kob.backend.service.account.RegisterService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author zeroac
 */
@RestController
@RequestMapping("/user/account") // 基础URL
@RequiredArgsConstructor // 自动为所有final字段生成构造函数
public class RegisterController extends BasisControl {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    private final RegisterService registerService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody Map<String, String> map) {
        String username = map.get("username");
        String password = map.get("password");
        String confirmedPassword = map.get("confirmedPassword");
        logger.info("username: {},password: {}", username, password);
        return ResponseEntity.ok(registerService.register(username, password, confirmedPassword));
    }
}
