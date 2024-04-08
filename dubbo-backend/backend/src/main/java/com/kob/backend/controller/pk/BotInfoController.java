package com.kob.backend.controller.pk;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zeroac
 * @RestController 注解是一个用于Spring框架的便利性注解，它结合了 @Controller 和 @ResponseBody 的功能。
 * 它标示着这个类为控制器（controller）类，并且其中的每个方法返回的不是视图名称，而是直接作为响应体返回的域对象。
 * 它通过允许开发者直接返回对象来简化了控制器的实现，这个返回的对象数据会直接作为JSON或XML格式写入HTTP响应中。
 */
@RestController
@RequestMapping("/pk")
public class BotInfoController {

    @RequestMapping("/getBotInfo")
    public List<Map<String, String>> getBotInfo() {
        List<Map<String, String>> list = new ArrayList<>();
        Map<String, String> kob1 = new HashMap<>();
        kob1.put("name", "jack");
        kob1.put("rating", "1100");
        Map<String, String> kob2 = new HashMap<>();
        kob2.put("name", "tom");
        kob2.put("rating", "1230");//ctrl+F9 热启动
        list.add(kob1);
        list.add(kob2);
        return list;
    }
}
