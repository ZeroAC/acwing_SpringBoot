package com.kob.backend.service.account;

import java.util.Map;

/**
 * @author zeroac
 */
public interface RegisterService {
    Map<String, String> register(String username, String password, String confirmedPassword);
}
