package com.kob.backend.service.pk.impl;

import com.kob.backend.service.pk.GameService;

/**
 * @author zeroac
 */
public class GameServiceImpl implements Runnable, GameService {
    private boolean g[][];
    private int userIdA;
    private int userIdB;


    public GameServiceImpl(int userIdA, int userIdB, boolean[][] g) {
        this.g = g;
        this.userIdA = userIdA;
        this.userIdB = userIdB;
    }


    @Override
    public void run() {
        start();
    }

    @Override
    public void start() {

    }

    @Override
    public void setNextStepA(Integer nextStepA) {

    }

    @Override
    public void setNextStepB(Integer nextStepB) {

    }

    @Override
    public boolean nextStep() {
        return false;
    }

    @Override
    public boolean judge() {
        return false;
    }

    @Override
    public void sendMove() {

    }

    @Override
    public void sendGameResult() {

    }
}
