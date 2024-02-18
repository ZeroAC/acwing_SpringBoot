package com.kob.backend.controller.user.account;

import com.kob.backend.service.account.LoginService;
import lombok.RequiredArgsConstructor;
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
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/token")
    public ResponseEntity<Map<String, String>> getToken(@RequestBody Map<String, String> map) {
        String username = map.get("username");
        String password = map.get("password");
        System.out.println(username + " " + password);
        return ResponseEntity.ok(loginService.getToken(username, password));
    }
}
