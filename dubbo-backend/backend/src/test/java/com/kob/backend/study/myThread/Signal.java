package com.kob.backend.study.myThread;

public class Signal {
    //volatile关键字保证该变量操作时为原子性操作，否则会出错死锁
    /*
     * 需要注意的是，signal++并不是一个原子操作，所以我们在实际开发中，
     * 会根据需要使用synchronized给它“上锁”，或者是使用AtomicInteger等原子类。
     * 并且上面的程序也并不是线程安全的，因为执行while语句后，可能当前线程就
     * 暂停等待时间片了，等线程醒来，可能signal已经大于等于5了。
     * */
    private static volatile int signal = 0;

    static class ThreadA implements Runnable {
        @Override
        public void run() {
            while (signal < 5) {
                if (signal % 2 == 0) {
                    System.out.println("threadA: " + signal);
                    signal++;
                }
            }
        }
    }

    static class ThreadB implements Runnable {
        @Override
        public void run() {
            while (signal < 5) {
                if (signal % 2 == 1) {
                    System.out.println("threadB: " + signal);
                    signal = signal + 1;
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new Thread(new ThreadA()).start();
        Thread.sleep(1000);
        new Thread(new ThreadB()).start();
    }
}
