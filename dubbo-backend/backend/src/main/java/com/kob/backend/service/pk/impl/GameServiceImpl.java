package com.kob.backend.service.pk.impl;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.constant.GameMapConstant;
import com.kob.backend.consumer.WebSocketServer;
import com.kob.backend.service.pk.GameService;
import com.kob.backend.service.pk.model.Cell;
import com.kob.backend.service.pk.model.Player;
import com.kob.backend.service.pk.model.Record;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author zeroac
 * 作为单独的线程开启，服务一局游戏
 */
@Service
@RequiredArgsConstructor
public class GameServiceImpl implements Runnable, GameService {
    private static final Logger logger = LoggerFactory.getLogger(GameServiceImpl.class);
    private boolean[][] g;
    private Player playerA;
    private Player playerB;
    //这两个变量的值会在主线程(WebSocketServer)设置,在本线程中监控读取，因此需要锁
    private Integer nextStepA = null;   // A的下一步操作
    private Integer nextStepB = null;   // B的下一步操作
    private ReentrantLock lock = new ReentrantLock();
    private String status = "playing";  // playing -> finished
    private String loser = "";  // all: 平局，A：A输，B：B输


    public GameServiceImpl(Player playerA, Player playerB, boolean[][] g) {
        this.g = g;
        this.playerA = playerA;
        this.playerB = playerB;
    }

    // 在主线程会读两个玩家的操作，并且玩家随时可能输入操作，存在读写冲突
    @Override
    public void setNextStepA(Integer nextStepA) {
        lock.lock();
        try {
            this.nextStepA = nextStepA;
            logger.info("setNextStepA:{}", nextStepA);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void setNextStepB(Integer nextStepB) {
        lock.lock();
        try {
            this.nextStepB = nextStepB;
            logger.info("setNextStepB:{}", nextStepB);
        } finally {
            lock.unlock();
        }
    }

    // 本线程开始执行
    @Override
    public void run() {
        // 一局游戏，地图大小总共13 * 14 = 182 ≈ 200，蛇每三步长一个格子，两条蛇总长度
        // 若200，每三步长一格，最多600步
        for (int i = 0; i < 650; i++) {
            if (nextStep()) {//10秒内等待游戏双方用户输入
                judge();
                if (status.equals("playing")) {
                    sendMove();
                } else {//非法输入，游戏结束
                    sendGameResult();
                    break;
                }
            } else {//10秒后依然没有输入，则游戏结束
                status = "finished";
                logger.info("游戏结束");
                lock.lock();
                // try finally是为了出异常也会抛锁
                try {
                    if (nextStepA == null && nextStepB == null) {
                        loser = "all";
                    } else if (nextStepA == null) {
                        loser = "A";
                    } else {
                        loser = "B";
                    }
                } finally {
                    lock.unlock();
                }
                // 游戏结束
                sendGameResult();
                break;
            }
        }
    }


    // 接收玩家的下一步操作
    @Override
    public boolean nextStep() {
        // 每秒五步操作，因此第一步操作是在200ms后判断是否接收到输入。并给地图初始化时间
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // 十秒内玩家双方需要给出下一步操作，因为会读玩家的nextStep操作，因此加锁
        for (int i = 0; i < 100; i++) {
            try {
                Thread.sleep(100);
                lock.lock();
                try {
                    if (nextStepA != null && nextStepB != null) {
                        playerA.getSteps().add(nextStepA);
                        playerB.getSteps().add(nextStepB);
                        return true;
                    }
                } finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }

    //判断蛇A的下一步操作是否合法
    private boolean checkNextStepValid(List<Cell> cellsA, List<Cell> cellsB) {
        int n = cellsA.size();
        Cell cell = cellsA.get(n - 1);
        // 蛇A的头不能撞墙
        if (g[cell.x][cell.y]) {
            return false;
        }
        // 蛇A的头不能撞到自己的身体
        for (int i = 0; i < n - 1; i++) {
            // 和蛇身是否重合
            if (cellsA.get(i).x == cell.x && cellsA.get(i).y == cell.y) {
                return false;
            }
        }
        // 蛇A的头不能撞到B的身体(注意，A和B的头永远撞不到一起)
        for (int i = 0; i < n - 1; i++) {
            // 和B蛇身是否重合
            if (cellsB.get(i).x == cell.x && cellsB.get(i).y == cell.y) {
                return false;
            }
        }
        return true;
    }

    //判断玩家A和B的下一步操作是否合法，若不合法则给出游戏结果
    @Override
    public void judge() {
        List<Cell> cellsA = playerA.getCells();
        List<Cell> cellsB = playerB.getCells();

        boolean validA = checkNextStepValid(cellsA, cellsB);
        boolean validB = checkNextStepValid(cellsB, cellsA);

        if (!validA || !validB) {
            status = "finished";
            if (!validA && !validB) {
                loser = "all";
            } else if (!validA) {
                loser = "A";
            } else {
                loser = "B";
            }
        }
    }


    private void sendMove() {
        // 因为需要读玩家的下一步操作，所以需要加锁
        lock.lock();
        try {
            JSONObject resp = new JSONObject();
            resp.put("event", "move");
            resp.put("a_direction", nextStepA);
            resp.put("b_direction", nextStepB);
            nextStepA = nextStepB = null;
            WebSocketServer.getUser(playerA.getId()).sendMessage(resp.toString());
            // 交换a和b的方向
            Object temp = resp.get("a_direction");
            resp.put("a_direction", resp.get("b_direction"));
            resp.put("b_direction", temp);
            WebSocketServer.getUser(playerB.getId()).sendMessage(resp.toString());
        } finally {
            lock.unlock();
        }
    }

    public String getGameResult(String player) {
        if ("all".equals(loser)) {
            return "all";
        }
        return player.equals(loser) ? "lose" : "win";
    }

    //将地图g转为字符串 便于存储
    private String getMapString() {
        int rows = GameMapConstant.ROWS.getValue();
        int columns = GameMapConstant.COLUMNS.getValue();
        StringBuilder res = new StringBuilder(rows * columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                res.append(g[i][j] ? '1' : '0');
            }
        }
        return res.toString();
    }

    private void saveToDataBase() {
        Record record = new Record(
                null,
                playerA.getId(),
                playerA.getSx(),
                playerA.getSy(),
                playerB.getId(),
                playerB.getSx(),
                playerB.getSy(),
                playerA.getStepsString(),
                playerB.getStepsString(),
                getMapString(),
                loser,
                new Date()
        );
        logger.info("saveToDataBase:{}", record);
        WebSocketServer.recordDao.insert(record);
    }


    public void sendGameResult() {
        //向两个Client返回游戏结果
        JSONObject resp = new JSONObject();
        resp.put("event", "result");
        resp.put("gameResult", getGameResult("A"));
        WebSocketServer.getUser(playerA.getId()).sendMessage(resp.toString());
        resp.put("gameResult", getGameResult("B"));
        WebSocketServer.getUser(playerB.getId()).sendMessage(resp.toString());
        saveToDataBase();//保存对局结果
    }

    @Override
    public Player getPlayerA() {
        return playerA;
    }

    @Override
    public Player getPlayerB() {
        return playerB;
    }
}
