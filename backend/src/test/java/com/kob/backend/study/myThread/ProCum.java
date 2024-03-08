package com.kob.backend.study.myThread;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * BlockingQueue是Java util.concurrent包下重要的数据结构，区别于普通的队列，
 * BlockingQueue提供了线程安全的队列访问方式，并发包下很多高级同步类的实现都
 * 是基于BlockingQueue实现的。
 */

public class ProCum {
    private int queueSize = 10;
    //阻塞队列
    private ArrayBlockingQueue<Integer> queue = new ArrayBlockingQueue<Integer>(queueSize);

    public static void main(String[] args) {
        ProCum test = new ProCum();
        Producer producer = test.new Producer();
        Consumer consumer = test.new Consumer();

        producer.start();
        consumer.start();
    }

    class Consumer extends Thread {

        @Override
        public void run() {
            consume();
        }

        private void consume() {
            while (true) {
                try {
                    queue.take();
                    System.out.println("从队列取走一个元素，队列剩余" + queue.size() + "个元素");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class Producer extends Thread {

        @Override
        public void run() {
            produce();
        }

        private void produce() {
            while (true) {
                try {
                    queue.put(1);
                    System.out.println("向队列取中插入一个元素，队列剩余空间：" + (queueSize - queue.size()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
