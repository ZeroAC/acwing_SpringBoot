package com.kob.backend.service.pk.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
