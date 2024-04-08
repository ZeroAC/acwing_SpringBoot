package com.kob.backend.study.myThread;

/**
 * 那我现在有一个需求，我想等A先执行完之后，再由B去执行，怎么办呢？
 * 最简单的方式就是使用一个“对象锁”：
 */
public class ObjectLock {
    private static Object lock = new Object();

    static class ThreadA implements Runnable {
        @Override
        public void run() {
            synchronized (lock) {
                for (int i = 0; i < 100; i++) {
                    System.out.println("Thread A " + i);
                }
            }
        }
    }

    static class ThreadB implements Runnable {
        @Override
        public void run() {
            synchronized (lock) {
                for (int i = 0; i < 100; i++) {
                    System.out.println("Thread B " + i);
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new Thread(new ThreadA()).start();
        //这里在主线程里使用sleep方法睡眠了10毫秒，是为了防止线程B先得到锁。
        // 因为如果同时start，线程A和线程B都是出于就绪状态，操作系统可能会先让B运行。
        // 这样就会先输出B的内容，然后B执行完成之后自动释放锁，线程A再执行。
        Thread.sleep(10);
        new Thread(new ThreadB()).start();
    }
}
