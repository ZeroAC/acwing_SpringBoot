package com.kob.backend.service.pk.impl;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.consumer.WebSocketServer;
import com.kob.backend.pojo.User;
import com.kob.backend.service.pk.GameMatchService;
import com.kob.backend.service.pk.model.GameMap;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author zeroac
 */
@Service
@RequiredArgsConstructor
public class GameMatchServiceImpl implements GameMatchService {
    private static final Logger logger = LoggerFactory.getLogger(GameMatchServiceImpl.class);
    //线程安全的匹配池
    private static final ConcurrentLinkedQueue<User> MATCH_POOL = new ConcurrentLinkedQueue<>();

    //最大游戏线程数
    private static final int MAX_GAME_THREADS = 100; // Adjust this value based on your server capacity
    //游戏线程池
    private static final ExecutorService gameExecutorService = Executors.newFixedThreadPool(MAX_GAME_THREADS);
    //地图数据生成
    private final GameMap gameMap;

    @Override
    public void startMatching(ConcurrentMap<Integer, WebSocketServer> users, User user) {
        logger.info("开始匹配");
        MATCH_POOL.add(user);
        attemptRandomMatch(users);
    }


    //处理匹配逻辑
    private void attemptRandomMatch(ConcurrentMap<Integer, WebSocketServer> users) {
        while (true) {
            User first = MATCH_POOL.poll();
            User second = MATCH_POOL.poll();
            if (first != null && second != null) {
                logger.info("匹配成功，first: {}, second: {}", first.getId(), second.getId());
                //生成一个新地图
                boolean[][] g = gameMap.generateMap();
                sendMatchMessage(users, first, second, g);
                sendMatchMessage(users, second, first, g);
                gameExecutorService.submit(new GameServiceImpl(first.getId(), second.getId(), g));
                return;
            }
            // 如果没有足够的玩家进行匹配，把已经取出的玩家重新放回队列
            if (first != null) {
                MATCH_POOL.offer(first);
            }
            if (second != null) {
                MATCH_POOL.offer(second);
            }
            // 休眠一段时间后再次尝试
            try {
                Thread.sleep(1000); // 这里可以根据实际情况调整休眠时间
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
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
    private void sendMatchMessage(ConcurrentMap<Integer, WebSocketServer> users, User a, User b, boolean[][] g) {
        JSONObject resp = new JSONObject();
        resp.put("event", "start-matching"); // 事件类型
        resp.put("opponentUsername", b.getUsername()); // 对手的用户名
        resp.put("opponentPhoto", b.getPhoto()); // 对手的头像
        resp.put("gameMap", g); // pk的游戏地图
        // 通过 WebSocket 向a发送匹配成功的消息
        users.get(a.getId()).sendMessage(resp.toJSONString());
    }

    @Override
    public void stopMatching(ConcurrentMap<Integer, WebSocketServer> users, User user) {
        logger.info("user:{} 取消匹配", user.getId());
        MATCH_POOL.remove(user);
    }
}
