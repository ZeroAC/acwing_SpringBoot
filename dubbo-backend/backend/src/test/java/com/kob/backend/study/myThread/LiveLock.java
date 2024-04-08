package com.kob.backend.study.myThread;

import java.util.concurrent.TimeUnit;

//活锁问题，活锁出现在两个线程互相改变对方的结束条件，最后谁也无法结束
public class LiveLock {
    static volatile int count = 10;

    // 无法解锁，一个想加到20，一个想减到0
    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            // 期望减到 0 退出循环
            while (count > 0) {
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                count--;
                System.out.println("t1 count: " + count);
            }
        }, "t1");
        Thread t2 = new Thread(() -> {
            // 期望超过 20 退出循环
            while (count < 20) {
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                count++;
                System.out.println("t2 count: " + count);
            }
        }, "t2");

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("main end");
    }
}