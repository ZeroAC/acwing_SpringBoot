package com.kob.backend.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kob.backend.constant.GameMapConstant;
import com.kob.backend.dao.RecordDao;
import com.kob.backend.dao.UserDao;
import com.kob.backend.pojo.User;
import com.kob.backend.service.dubbo.MatchingService;
import com.kob.backend.service.pk.GameService;
import com.kob.backend.service.pk.impl.GameServiceImpl;
import com.kob.backend.service.pk.model.GameMap;
import com.kob.backend.service.pk.model.Player;
import com.kob.backend.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    //用户Dao
    private static UserDao userDao;
    //游戏结束时，存储对局信息Dao
    public static RecordDao recordDao;

    private static MatchingService matchingService;

    //最大游戏线程数
    private static final int MAX_GAME_THREADS = 100; // Adjust this value based on your server capacity
    //游戏线程池
    private static final ExecutorService gameExecutorService = Executors.newFixedThreadPool(MAX_GAME_THREADS);
    //地图数据生成
    private static GameMap gameMap;

    @Autowired
    public void setGameMap(GameMap gameMap) {
        WebSocketServer.gameMap = gameMap;
    }

    @Autowired
    public void setMatchingService(MatchingService service) {
        matchingService = service;
    }

    //当前的用户所在的游戏对局
    private GameService gameService = null;

    public void setGameService(GameService service) {
        gameService = service;
    }

    // 静态 setter 方法来设置共享的 UserDao 实例
    @Autowired
    public void setUserDao(UserDao userDao) {
        WebSocketServer.userDao = userDao;
    }

    @Autowired
    public void setRecordDao(RecordDao recordDao) {
        WebSocketServer.recordDao = recordDao;
    }

    //存储所有用户链接(与线程安全有关的哈希表，将userID映射到相应用户的WebSocketServer)
    private static final ConcurrentHashMap<Integer, WebSocketServer> USERS = new ConcurrentHashMap<>();

    // 当前用户
    private User user;

    // 前端后端互相发信息,每个链接用session维护
    private Session session;

    // 使用单例模式的ObjectMapper, 调试用,结构体转JSON
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();


    // 用来获取指定用户ID的WebSocketServer实例
    public static WebSocketServer getUser(Integer userId) {
        return USERS.get(userId);
    }

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
        USERS.put(userId, this);
    }

    /**
     * 当客户端关闭连接时触发。
     */
    @OnClose
    public void onClose() {
        // 在这里处理连接关闭时的操作，例如从会话管理器中移除会话、清理资源等
        logger.info("客户{}断开连接", this.user.getId());
        // 从会话管理器中移除会话
        if (this.user != null) {
            USERS.remove(this.user.getId());
        }
    }

    /**
     * 当服务器接收到客户端发送的消息时触发。
     * 作为websocket的路由
     *
     * @param message 客户端发送的消息
     * @param session 客户端与服务器端的WebSocket会话对象
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        // 在这里处理从客户端收到的消息，例如广播消息、消息处理等
        logger.info("收到客户端消息：{}", message);
        JSONObject jsonObject = JSON.parseObject(message);
        String event = jsonObject.getString("event");
        if ("start-matching".equals(event)) {
            logger.info("用户{}开始匹配", this.user.getId());
            //向匹配微服务添加一个用户，当微服务匹配成功时，则会调用startGame接口来启动游戏
            matchingService.addPlayer(this.user.getId(), 1500);
        } else if ("stop-matching".equals(event)) {
            logger.info("用户{}停止匹配", this.user.getId());
            matchingService.removePlayer(this.user.getId());
        } else if ("move".equals(event)) {
            Integer direction = jsonObject.getInteger("direction");
            logger.info("接收到用户{}的移动方向为{}", this.user.getId(), direction);
            if (gameService.getPlayerA().getId().equals(user.getId())) {
                gameService.setNextStepA(direction);
            } else if (gameService.getPlayerB().getId().equals(user.getId())) {
                gameService.setNextStepB(direction);
            }
        }
    }
    
    //匹配完成后a和b开始一局游戏
    public static void startGame(Integer aId, Integer bId){
        logger.info("匹配成功，aId: {}, bId: {}", aId, bId);
        //生成一个新地图
        boolean[][] g = gameMap.generateMap();
        //生成对局玩家信息
        Player playerA = new Player(aId, GameMapConstant.ROWS.getValue() - 2, 1, new ArrayList<>());
        Player playerB = new Player(bId, 1, GameMapConstant.COLUMNS.getValue() - 2, new ArrayList<>());
        User a = userDao.selectById(aId);
        User b = userDao.selectById(bId);
        //发送匹配成功消息及对局信息
        sendMatchMessage(a, b, playerA, playerB, g);
        sendMatchMessage(b, a, playerB, playerA, g);
        //新开一局游戏
        GameService openGameService = new GameServiceImpl(playerA, playerB, g);
        //提交游戏到线程池中执行
        gameExecutorService.submit((Runnable) openGameService);
        //设置双方的游戏对局
        USERS.get(aId).setGameService(openGameService);
        USERS.get(bId).setGameService(openGameService);
    }


    //发送匹配成功的对手消息给双方
    private static void sendMatchMessage(User a, User b, Player playerA, Player playerB, boolean[][] g) {
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
        USERS.get(a.getId()).sendMessage(resp.toJSONString());
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