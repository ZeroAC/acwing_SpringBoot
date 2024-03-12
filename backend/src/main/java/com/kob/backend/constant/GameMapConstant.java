package com.kob.backend.constant;

import lombok.Getter;

/**
 * @author zeroac
 */

@Getter
public enum GameMapConstant {
    ROWS(13, "游戏地图的行数"),
    COLUMNS(14, "游戏地图的列数"),
    INNER_WALLS_COUNT(50, "游戏地图中内墙的数量");
    private final int value;
    private final String name;

    GameMapConstant(int value, String name) {
        this.value = value;
        this.name = name;
    }

}
