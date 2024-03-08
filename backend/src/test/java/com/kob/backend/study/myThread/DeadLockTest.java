package com.kob.backend.study.myThread;

//死锁
//指两个或两个以上的进程（或线程）在执行过程中，因争夺资源而造成的一种互相等待的现象
// （至少两个资源），若无外力作用，它们都将无法推进下去。此时称系统处于死锁状态或系统
// 产生了死锁，这些永远在互相等待的进程称为死锁进程。
public class DeadLockTest {

    public static final Object resource1 = new Object();
    public static final Object resource2 = new Object();

    public static void main(String[] args) {

        //持有1，等待2
        new Thread(() -> {
            synchronized (resource1) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "：等待");
                synchronized (resource2) {
                    System.out.println(Thread.currentThread().getName() + "：可以执行");
                }
            }
        }, "A").start();

        //持有2，等待1
        new Thread(() -> {
            synchronized (resource2) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "：等待");
                synchronized (resource1) {
                    System.out.println(Thread.currentThread().getName() + "：可以执行");
                }
            }
        }, "B").start();
    }

}
