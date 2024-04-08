package com.kob.backend.study.myThread;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

//线程饥饿
public class ThreadStarvingTest {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        executorService.submit(() -> {
            System.out.println(currentThreadName() + "接待客人");
            Future<String> cook = executorService.submit(() -> {
                System.out.println(currentThreadName() + "做回锅肉");
                return "回锅肉";
            });

            try {
                System.out.println(currentThreadName() + "上" + cook.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });

        executorService.submit(() -> {
            System.out.println(currentThreadName() + "接待客人");
            Future<String> cook = executorService.submit(() -> {
                System.out.println(currentThreadName() + "做鱼香肉丝");
                return "鱼香肉丝";
            });

            try {
                System.out.println(currentThreadName() + "上" + cook.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });

    }

    public static String currentThreadName() {
        return Thread.currentThread().getName();
    }

}
