package com.kob.backend.service.pk.impl;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.constant.GameMapConstant;
import com.kob.backend.consumer.WebSocketServer;
import com.kob.backend.pojo.User;
import com.kob.backend.service.pk.GameMatchService;
import com.kob.backend.service.pk.GameService;
import com.kob.backend.service.pk.model.GameMap;
import com.kob.backend.service.pk.model.Player;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        while (MATCH_POOL.size() >= 2) {
            User first = MATCH_POOL.poll();
            User second = MATCH_POOL.poll();
            if (first != null && second != null) {
                logger.info("匹配成功，first: {}, second: {}", first.getId(), second.getId());
                //生成一个新地图
                boolean[][] g = gameMap.generateMap();
                //生成对局玩家信息
                Player playerA = new Player(first.getId(), GameMapConstant.ROWS.getValue() - 2, 1, new ArrayList<>());
                Player playerB = new Player(second.getId(), 1, GameMapConstant.COLUMNS.getValue() - 2, new ArrayList<>());
                //发送匹配成功消息及对局信息
                sendMatchMessage(users, first, second, playerA, playerB, g);
                sendMatchMessage(users, second, first, playerB, playerA, g);
                //创建游戏对局
                GameService gameService = new GameServiceImpl(playerA, playerB, g);
                //提交游戏到线程池中执行
                gameExecutorService.submit((Runnable) gameService);
                //设置双方的游戏对局
                users.get(first.getId()).setGameService(gameService);
                users.get(second.getId()).setGameService(gameService);
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
    private void sendMatchMessage(ConcurrentMap<Integer, WebSocketServer> users, User a, User b, Player playerA, Player playerB, boolean[][] g) {
        JSONObject resp = new JSONObject();
        resp.put("event", "start-matching"); // 事件类型
        resp.put("opponentUsername", b.getUsername()); // 对手的用户名
        resp.put("opponentPhoto", b.getPhoto()); // 对手的头像
        //游戏对局信息
        JSONObject gameResp = new JSONObject();//对局游戏信息
        gameResp.put("map", g);
        gameResp.put("id", playerA.getId());
        gameResp.put("sx", playerA.getSx());
        gameResp.put("sy", playerA.getSy());
        gameResp.put("opponentId", playerB.getId());
        gameResp.put("opponentSx", playerB.getSx());
        gameResp.put("opponentSy", playerB.getSy());
        resp.put("game", gameResp);
        // 通过 WebSocket 向a发送匹配成功的消息
        users.get(a.getId()).sendMessage(resp.toJSONString());
    }

    @Override
    public void stopMatching(ConcurrentMap<Integer, WebSocketServer> users, User user) {
        logger.info("user:{} 取消匹配", user.getId());
        MATCH_POOL.remove(user);
    }
}
