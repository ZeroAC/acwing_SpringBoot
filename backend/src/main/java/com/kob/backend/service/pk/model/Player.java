package com.kob.backend.service.pk.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zeroac
 * 匹配成功后，存储玩家信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player {
    private Integer id;
    private Integer sx;
    private Integer sy;
    // 存储玩家的每一步方向，用于后续的回放对局
    private List<Integer> steps;

    // 检查当前回合，蛇的长度是否会增加
    private boolean checkTailIncreasing(int step) {
        if (step <= 10) {
            return true;
        }
        return step % 3 == 1;
    }

    //获取游戏蛇身
    public List<Cell> getCells() {
        List<Cell> res = new ArrayList<>();

        int[] dx = {-1, 0, 1, 0};
        int[] dy = {0, 1, 0, -1};
        int x = sx, y = sy;
        res.add(new Cell(x, y));
        int step = 0;
        for (int d : steps) {
            x += dx[d];
            y += dy[d];
            res.add(new Cell(x, y));
            if (!checkTailIncreasing(++step)) {
                res.remove(0);
            }
        }
        return res;
    }
}
