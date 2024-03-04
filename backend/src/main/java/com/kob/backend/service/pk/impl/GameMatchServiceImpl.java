package com.kob.backend.service.pk.impl;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.consumer.WebSocketServer;
import com.kob.backend.pojo.User;
import com.kob.backend.service.pk.GameMatchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author zeroac
 */
@Service
public class GameMatchServiceImpl implements GameMatchService {
    private static final Logger logger = LoggerFactory.getLogger(GameMatchServiceImpl.class);
    //线程安全的匹配池
    private static final ConcurrentLinkedQueue<User> MATCH_POOL = new ConcurrentLinkedQueue<>();

    @Override
    public void startMatching(ConcurrentMap<Integer, WebSocketServer> users, User user) {
        logger.info("开始匹配");
        MATCH_POOL.add(user);

        if (MATCH_POOL.size() >= 2) {
            User first = null;
            User second = null;

            //尝试随机匹配
            List<User> usersList = new ArrayList<>(MATCH_POOL);
            while (usersList.size() >= 2) {
                int firstIndex = ThreadLocalRandom.current().nextInt(usersList.size());
                first = usersList.remove(firstIndex);

                int secondIndex = ThreadLocalRandom.current().nextInt(usersList.size());
                second = usersList.remove(secondIndex);

                // 尝试从匹配池中移除选中的用户
                if (MATCH_POOL.remove(first) && MATCH_POOL.remove(second)) {
                    logger.info("匹配成功，first: {}, second: {}", first.getId(), second.getId());
                    sendMatchMessage(users, first, second);
                    sendMatchMessage(users, second, first);
                    break;
                } else {
                    // 如果移除失败（可能因为用户已被其他线程匹配），则重新尝试
                    usersList.add(first);
                    usersList.add(second);
                }
            }
        }
    }

    /**
     * 发送匹配消息。
     * 当用户a和b匹配成功后，调用此方法向a发送匹配成功的消息。
     *
     * @param users 保存所有用户与其对应 WebSocket 连接的并发映射。
     * @param a     匹配方
     * @param b     被匹配方
     */
    private void sendMatchMessage(ConcurrentMap<Integer, WebSocketServer> users, User a, User b) {
        JSONObject resp = new JSONObject();
        resp.put("event", "start-matching"); // 事件类型
        resp.put("opponentUsername", b.getUsername()); // 对手的用户名
        resp.put("opponentPhoto", b.getPhoto()); // 对手的头像
        // 通过 WebSocket 向a发送匹配成功的消息
        users.get(a.getId()).sendMessage(resp.toJSONString());
    }

    @Override
    public void stopMatching(ConcurrentMap<Integer, WebSocketServer> users, User user) {
        logger.info("user:{} 取消匹配", user.getId());
        MATCH_POOL.remove(user);
    }
}
