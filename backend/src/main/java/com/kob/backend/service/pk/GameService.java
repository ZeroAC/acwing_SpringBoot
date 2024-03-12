package com.kob.backend.service.pk;

import com.kob.backend.service.pk.model.Player;

/**
 * 提供了玩家对战游戏的服务接口定义。
 *
 * @author zeroac
 */
public interface GameService {

    /**
     * 设置玩家A的下一步动作。
     *
     * @param nextStepA 玩家A即将执行的动作。
     */
    void setNextStepA(Integer nextStepA);

    /**
     * 设置玩家B的下一步动作。
     *
     * @param nextStepB 玩家B即将执行的动作。
     */
    void setNextStepB(Integer nextStepB);

    /**
     * 接收两个玩家的下一步操作，执行游戏的下一步
     *
     * @return 如果获取到了两个玩家的下一步操作返回true，否则返回false。
     */
    boolean nextStep();

    /**
     * 判断游戏当前的局面是否已经结束。
     *
     * @return 如果游戏结束返回true，否则返回false。
     */
    boolean judge();

    Player getPlayerA();

    Player getPlayerB();

}