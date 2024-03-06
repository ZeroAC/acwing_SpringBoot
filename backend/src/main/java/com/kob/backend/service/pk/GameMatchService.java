package com.kob.backend.service.pk;

import com.kob.backend.consumer.WebSocketServer;
import com.kob.backend.pojo.User;

import java.util.concurrent.ConcurrentMap;

/**
 * @author zeroac
 */
public interface GameMatchService {
    /**
     * 开始匹配方法。
     * 当一个用户想要开始游戏匹配时调用此方法。
     *
     * @param users 所有正在连接的用户和他们的 WebSocket 连接。
     * @param user  想要开始匹配的用户。
     */
    void startMatching(ConcurrentMap<Integer, WebSocketServer> users, User user);

    /**
     * 停止匹配方法。
     * 如果用户在匹配过程中想要停止匹配，可以调用此方法。
     *
     * @param users 所有正在连接的用户和他们的 WebSocket 连接。
     * @param user  想要停止匹配的用户。
     */
    void stopMatching(ConcurrentMap<Integer, WebSocketServer> users, User user);
}
