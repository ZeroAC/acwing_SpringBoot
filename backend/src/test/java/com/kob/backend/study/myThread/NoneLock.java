package com.kob.backend.study.myThread;

/**
 * 执行这个程序，你会在控制台看到，线程A和线程B各自独立工作，输出自己的打印值。
 * 如下是我的电脑上某一次运行的结果。每一次运行结果都会不一样。
 */
public class NoneLock {

    static class ThreadA implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 100; i++) {
                System.out.println("Thread A " + i);
            }
        }
    }

    static class ThreadB implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 100; i++) {
                System.out.println("Thread B " + i);
            }
        }
    }

    public static void main(String[] args) {
        new Thread(new ThreadA()).start();
        new Thread(new ThreadB()).start();
    }
}
