package com.kob.backend.service.pk.impl;

import com.kob.backend.service.pk.GameService;

/**
 * @author zeroac
 */
public class GameServiceImpl implements Runnable, GameService {
    private boolean g[][];
    private int userIdA;
    private int userIdB;

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

    public GameServiceImpl(int userIdA, int userIdB, boolean[][] g) {
        this.g = deepCopyBooleanArray(g);
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
