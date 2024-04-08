package com.kob.backend.service.bot.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kob.backend.dao.BotDao;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.User;
import com.kob.backend.service.bot.BotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zeroac
 */
@Service
@RequiredArgsConstructor // 自动为所有final字段生成构造函数
public class BotServiceImpl extends ServiceImpl<BotDao, Bot> implements BotService {
    private final BotDao botDao;

    public Date getNowTime() {
        return new Date();
    }

    @Override
    public Map<String, String> add(User user, Map<String, String> data) {
        String title = data.get("title");
        String description = data.get(("description"));
        String content = data.get("content");

        Map<String, String> map = new HashMap<>();

        if (title == null || title.isEmpty()) {
            map.put("error_message", "标题不能为空");
            return map;
        }

        if (title.length() > 100) {
            map.put("error_message", "标题长度不能大于100");
            return map;
        }

        if (description == null || description.isEmpty()) {
            description = "这个用户很懒，什么也没有留下~";
        }

        if (description.length() > 300) {
            map.put("error_message", "Bot描述的长度不能大于300");
            return map;
        }

        if (content == null || content.isEmpty()) {
            map.put("error_message", "代码不能为空");
            return map;
        }

        if (content.length() > 10000) {
            map.put("error_message", "代码长度不能超过10000");
            return map;
        }
        Bot bot = new Bot(null, user.getId(), title, description, content, 1500, getNowTime(), getNowTime());


        botDao.insert(bot);
        map.put("error_message", "success");

        return map;
    }

    @Override
    public Map<String, String> remove(User user, Map<String, String> data) {
        int botId = Integer.parseInt(data.get("bot_id"));
        Bot bot = botDao.selectById(botId);
        Map<String, String> map = new HashMap<>();

        if (bot == null) {
            map.put("error_message", "Bot不存在或已被删除");
            return map;
        }

        if (!bot.getUserId().equals(user.getId())) {
            map.put("error_message", "没有权限删除该Bot");
            return map;
        }

        botDao.deleteById(botId);

        map.put("error_message", "success");
        return map;
    }

    @Override
    public Map<String, String> update(User user, Map<String, String> data) {
        int botId = Integer.parseInt(data.get("bot_id"));

        String title = data.get("title");
        String description = data.get("description");
        String content = data.get("content");

        Map<String, String> map = new HashMap<>();

        if (title == null || title.isEmpty()) {
            map.put("error_message", "标题不能为空");
            return map;
        }

        if (title.length() > 100) {
            map.put("error_message", "标题长度不能大于100");
            return map;
        }

        if (description == null || description.isEmpty()) {
            description = "这个用户很懒，什么也没有留下~";
        }

        if (description.length() > 300) {
            map.put("error_message", "Bot描述的长度不能大于300");
            return map;
        }

        if (content == null || content.isEmpty()) {
            map.put("error_message", "代码不能为空");
            return map;
        }

        if (content.length() > 10000) {
            map.put("error_message", "代码长度不能超过10000");
        }

        Bot bot = botDao.selectById(botId);

        if (bot == null) {
            map.put("error_message", "Bot不存在或已经被删除");
            return map;
        }

        if (!bot.getUserId().equals(user.getId())) {
            map.put("error_message", "没有权限修改该Bot");
            return map;
        }


        botDao.updateById(new Bot(
                bot.getId(),
                user.getId(),
                title,
                description,
                content,
                bot.getRating(),
                bot.getCreateTime(),
                getNowTime()
        ));

        map.put("error_message", "success");

        return map;
    }

    @Override
    public Page<Bot> getList(User user, Page<Bot> page) {
        // 使用MyBatis-Plus的查询构造器进行分页查询
        return lambdaQuery().eq(Bot::getUserId, user.getId()).page(page);
    }
}
