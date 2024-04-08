package com.kob.backend.service.account;

import com.kob.backend.pojo.User;

import java.util.Map;

/**
 * @author zeroac
 */
public interface InfoService {

    Map<String, String> getInfo(User user);
}
