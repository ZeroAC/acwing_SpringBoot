package com.kob.backend.controller.pk;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zeroac
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
        kob2.put("rating", "1500");
        list.add(kob1);
        list.add(kob2);
        return list;
    }
}
