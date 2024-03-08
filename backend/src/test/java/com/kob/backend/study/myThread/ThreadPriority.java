package com.kob.backend.study.myThread;

import java.util.stream.IntStream;

public class ThreadPriority {
    public static class T1 extends Thread {
        @Override
        public void run() {
            super.run();
            System.out.println(String.format("当前执行的线程是：%s，优先级：%d",
                    Thread.currentThread().getName(),
                    Thread.currentThread().getPriority()));
        }
    }

    //高优先级的线程将会比低优先级的线程有更高的几率得到执行
    //Java中的优先级来说不是特别的可靠，Java程序中对线程所设置的优先级
    // 只是给操作系统一个建议，操作系统不一定会采纳。而真正的调用顺序，是由操作系统
    // 的线程调度算法决定的。
    public static void main(String[] args) {
        IntStream.range(1, 10).forEach(i -> {
            Thread thread = new Thread(new T1());
            thread.setPriority(i);
            thread.start();
        });
    }
}
