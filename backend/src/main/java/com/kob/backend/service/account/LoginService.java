package com.kob.backend.service.account;

import java.util.Map;

/**
 * @author zeroac
 */
public interface LoginService {
    public Map<String, String> getToken(String username, String password);
}
