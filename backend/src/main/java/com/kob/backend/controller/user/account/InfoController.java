package com.kob.backend.controller.user.account;

import com.kob.backend.controller.commons.BasisControl;
import com.kob.backend.pojo.User;
import com.kob.backend.service.account.InfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author zeroac
 */
@RestController
@RequiredArgsConstructor // 自动为所有final字段生成构造函数
@RequestMapping("/user/account") // 基础URL
public class InfoController extends BasisControl {
    private final InfoService infoService;

    @GetMapping("/info")
    public Map<String, String> getInfo() {
        User user = getUser();
        return infoService.getInfo(user);
    }
}

