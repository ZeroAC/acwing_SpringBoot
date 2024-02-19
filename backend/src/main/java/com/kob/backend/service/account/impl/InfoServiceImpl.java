package com.kob.backend.service.account.impl;

import com.kob.backend.pojo.User;
import com.kob.backend.service.account.InfoService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zeroac
 */
@Service
public class InfoServiceImpl implements InfoService {


    @Override
    public Map<String, String> getInfo(User user) {
        Map<String, String> map = new HashMap<>();
        map.put("error_message", "success");
        map.put("id", user.getId().toString());
        map.put("username", user.getUsername());
        map.put("photo", user.getPhoto());
        return map;
    }
}
