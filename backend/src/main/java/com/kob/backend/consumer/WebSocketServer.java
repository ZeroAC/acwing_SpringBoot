package com.kob.backend.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kob.backend.dao.UserDao;
import com.kob.backend.pojo.User;
import com.kob.backend.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket服务端点类。
 * 使用@Component注解标记，以便Spring能够扫描并将其作为Bean进行管理。
 * 使用@ServerEndpoint注解标记这个类为WebSocket的服务端点，客户端可以通过“/websocket/{token}”的URL来连接。
 * 其中{token}是一个路径参数，客户端连接时需要提供，可用于识别用户或进行权限校验。
 *
 * @author zeroac
 */
@Component
@ServerEndpoint("/websocket/{token}")
public class WebSocketServer {
    //日志
    private static final Logger logger = LoggerFactory.getLogger(WebSocketServer.class);
    // 因为 WebSocket 服务器端点不是由 Spring 管理，所以设置为静态 从而单例
    private static volatile UserDao userDao;

    // 静态 setter 方法来设置共享的 UserDao 实例
    @Autowired
    public void setUserDao(UserDao userDao) {
        WebSocketServer.userDao = userDao;
    }

    //存储所有用户链接(与线程安全有关的哈希表，将userID映射到相应用户的WebSocketServer)
    private static ConcurrentHashMap<Integer, WebSocketServer> users = new ConcurrentHashMap<>();

    // 当前用户
    private User user;

    // 前端后端互相发信息,每个链接用session维护
    private Session session;

    // 使用单例模式的ObjectMapper
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    String stringify(Object obj) throws JsonProcessingException {
        return OBJECT_MAPPER.writeValueAsString(obj);
    }

    /**
     * 当客户端打开连接时触发。
     *
     * @param session 客户端与服务器端的WebSocket会话对象
     * @param token   通过URL路径传递的token
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        // 在这里处理连接建立时的操作，例如添加会话到会话管理器、验证token等
        this.session = session;
        // 从token中获取用户ID
        Integer userId = JwtUtil.getUserId(token);
        // 从数据库中获取用户信息
        User connectedUser = userDao.selectById(userId);
        if (connectedUser == null) {
            String error = "用户认证失败";
            session.getAsyncRemote().sendText(stringify(error));
            session.close();
            return;
        }
        logger.info("用户{}连接成功", connectedUser.getId());

        this.user = connectedUser;
        // 将用户链接添加到会话管理器 由于是哈希表以id为键，保证了每个用户只能有一个socket链接
        users.put(userId, this);
    }

    /**
     * 当客户端关闭连接时触发。
     */
    @OnClose
    public void onClose() {
        // 在这里处理连接关闭时的操作，例如从会话管理器中移除会话、清理资源等
        logger.info("客户端断开连接");
        // 从会话管理器中移除会话
        if (this.user != null) {
            users.remove(this.user.getId());
        }
    }

    /**
     * 当服务器接收到客户端发送的消息时触发。
     *
     * @param message 客户端发送的消息
     * @param session 客户端与服务器端的WebSocket会话对象
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        // 在这里处理从客户端收到的消息，例如广播消息、消息处理等
        logger.info("收到客户端消息：{}", message);
    }

    //后端向前端发送消息
    public void sendMessage(String message) {
        //防止多个线程同时向前端发送消息，竞争session。异步发送，线程安全，不阻塞
        this.session.getAsyncRemote().sendText(message, result -> {
            if (result.isOK()) {
                logger.info("消息发送成功：{}", message);
            } else {
                Throwable exception;
                exception = result.getException();
                logger.error("消息发送失败：{}", exception.getMessage());
            }
        });
    }

    /**
     * 当WebSocket会话发生错误时触发。
     *
     * @param session 出现错误的客户端与服务器端的WebSocket会话对象
     * @param error   异常对象
     */
    @OnError
    public void onError(Session session, Throwable error) {
        // 在这里处理会话错误，记录日志、通知管理员等
        logger.error("WebSocket会话发生错误：{}", error.getMessage());
    }

}