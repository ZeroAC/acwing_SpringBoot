package com.kob.backend.controller.user.bot;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kob.backend.controller.commons.BasisControl;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.User;
import com.kob.backend.service.bot.BotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author zeroac
 */
@RestController
@RequiredArgsConstructor // 自动为所有final字段生成构造函数
@RequestMapping("/user/bot") // 基础URL
public class BotControl extends BasisControl {
    private final BotService botService;

    @PostMapping("/add")
    public ResponseEntity<Map<String, String>> add(@RequestBody Map<String, String> map) {
        logger.info("add Bot map:{}", map);
        User user = getUser();
        ResponseEntity<Map<String, String>> response = getResponse(botService.add(user, map));
        logger.info("add Bot response:{}", response);
        return response;
    }

    @PostMapping("/remove")
    public ResponseEntity<Map<String, String>> remove(@RequestBody Map<String, String> map) {
        User user = getUser();
        return getResponse(botService.remove(user, map));
    }

    @PostMapping("/update")
    public ResponseEntity<Map<String, String>> update(@RequestBody Map<String, String> map) {
        User user = getUser();
        return getResponse(botService.update(user, map));
    }

    /**
     * 获取分页的Bot列表。
     * 通过HTTP GET请求调用此端点，用于检索分页的Bot数据。
     * 请求的分页参数由URL的查询参数提供。
     *
     * @param page  请求的当前页码。
     * @param limit 每页显示的记录数。
     * @return 返回一个包含分页Bot数据的ResponseEntity对象。
     */
    @GetMapping("/getList")
    public ResponseEntity<Page<Bot>> getList(@RequestParam("page") int page, @RequestParam("limit") int limit) {
        User user = getUser(); // 获取当前请求的用户信息

        // 使用请求的分页参数创建一个新的Page对象
        Page<Bot> botPage = new Page<>(page, limit);

        // 调用Service层方法，传入用户信息和分页参数，执行分页查询
        Page<Bot> result = botService.getList(user, botPage);

        // 将查询结果包装在ResponseEntity中，并返回给客户端
        return ResponseEntity.ok(result);
    }
}
