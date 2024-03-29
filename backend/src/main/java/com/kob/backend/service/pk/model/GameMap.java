package com.kob.backend.service.pk.model;

import com.kob.backend.constant.GameMapConstant;
import lombok.Data;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * @author zeroac
 * 匹配成功后，生成地图供玩家PK
 */
@Data
@Component
public class GameMap {
    private static final Random RANDOM = new Random();
    private final int rows;
    private final int cols;
    private final int innerWallsCount;
    @Getter
    private final boolean[][] g;
    private static final int[] DX = {-1, 0, 1, 0};
    private static final int[] DY = {0, 1, 0, -1};


    public GameMap() {
        this.rows = GameMapConstant.ROWS.getValue();
        this.cols = GameMapConstant.COLUMNS.getValue();
        this.innerWallsCount = GameMapConstant.INNER_WALLS_COUNT.getValue();
        this.g = new boolean[rows][cols];
    }

    //判断从(sx, sy)到(ex, ey)是否连通
    public boolean isConnected(int sx, int sy, int ex, int ey) {
        if (sx == ex && sy == ey) {
            return true;
        }
        if (sx < 0 || sy < 0 || sx >= this.g.length || sy >= this.g[0].length || this.g[sx][sy]) {
            return false;
        }

        this.g[sx][sy] = true;

        for (int i = 0; i < 4; i++) {
            int nx = sx + DX[i];
            int ny = sy + DY[i];
            if (isConnected(nx, ny, ex, ey)) {
                this.g[sx][sy] = false;
                return true;
            }
        }
        this.g[sx][sy] = false;
        return false;
    }

    //画地图 返回值为是否绘制成功
    public boolean draw() {
        //初始化一圈墙，内部为空
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                this.g[i][j] = (i == 0 || i == this.rows - 1 || j == 0 || j == this.cols - 1);
            }
        }
        //创建内部障碍物(公平起见，中心对称)，但不能覆盖起点
        for (int i = 0; i < this.innerWallsCount / 2; i++) {
            for (int j = 0; j < 1000; j++) {
                int x = RANDOM.nextInt(this.rows);
                int y = RANDOM.nextInt(this.cols);
                if (this.g[x][y] || this.g[this.rows - 1 - x][this.cols - 1 - y]) {
                    continue;
                }
                if (x == this.rows - 2 && y == 1 || x == 1 && y == this.cols - 2) {
                    continue;
                }
                this.g[x][y] = this.g[this.rows - 1 - x][this.cols - 1 - y] = true;
                break;
            }
        }
        //判断从左下角到右上角是否连通
        return isConnected(this.rows - 2, 1, 1, this.cols - 2);
    }

    private boolean[][] deepCopyBooleanArray(boolean[][] original) {
        if (original == null) {
            return new boolean[0][0];
        }

        final boolean[][] result = new boolean[original.length][];
        for (int i = 0; i < original.length; i++) {
            result[i] = original[i] != null ? new boolean[original[i].length] : null;
            if (original[i] != null) {
                System.arraycopy(original[i], 0, result[i], 0, original[i].length);
            }
        }
        return result;
    }


    //生成有效地图 可能多个匹配成功的同时调用，故而需要 同步 线程安全
    public synchronized boolean[][] generateMap() {
        for (int i = 0; i < 1000; i++) {
            if (draw()) {
                break;
            }
        }
        return deepCopyBooleanArray(this.g);
    }
}
