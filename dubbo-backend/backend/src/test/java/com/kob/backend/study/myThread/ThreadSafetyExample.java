package com.kob.backend.study.myThread;

import java.util.ArrayList;
import java.util.List;

public class ThreadSafetyExample {

    // 共享资源
    private int sharedResource = 0;

    // 同步方法
    private synchronized void increment() {
        for (int i = 0; i < 100000; i++) {
            sharedResource++;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // 创建线程安全示例对象
        ThreadSafetyExample example = new ThreadSafetyExample();

        // 创建并启动多个线程
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Thread t = new Thread(example::increment);
            threads.add(t);
            t.start();
        }

        // 等待所有线程执行完成
        for (Thread t : threads) {
            t.join();
        }

        System.out.println("Result: " + example.sharedResource);
    }
}
