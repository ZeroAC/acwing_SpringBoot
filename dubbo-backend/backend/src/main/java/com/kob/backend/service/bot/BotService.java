package com.kob.backend.service.bot;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.User;

import java.util.Map;

/**
 * @author zeroac
 */
public interface BotService extends IService<Bot> {
    Map<String, String> add(User user, Map<String, String> data);

    Map<String, String> remove(User user, Map<String, String> data);

    Map<String, String> update(User user, Map<String, String> data);

    Page<Bot> getList(User user, Page<Bot> page);
}
