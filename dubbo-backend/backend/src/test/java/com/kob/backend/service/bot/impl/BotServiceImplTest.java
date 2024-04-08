package com.kob.backend.service.bot.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kob.backend.dao.UserDao;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BotServiceImplTest {
    @Autowired
    private BotServiceImpl botService;

    @Autowired
    private UserDao userDao;

    String stringify(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    @Test
    void getList() throws JsonProcessingException {
        User user = userDao.selectByUsername("yxc");
        System.out.println(user);
        int page = 1;
        int limit = 4;
        Page<Bot> botPage = new Page<>(page, limit); // 创建Page对象
        Page<Bot> result = botService.getList(user, botPage);

        System.out.println(stringify(result));
        Assertions.assertNotNull(result, "结果不应该为null");
        Assertions.assertEquals(limit, result.getRecords().size(), "结果数量应该等于请求的限制数量");
    }


}
