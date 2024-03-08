package com.kob.backend.study.myThread;

import java.util.concurrent.*;

/**
 * 并行计算
 * 我们假设有一个计算任务，需要将1到100,000之间的所有数字相加。首先，我们创建了一个固定
 * 线程数的线程池（这里使用了 Executors.newFixedThreadPool 方法，创建一个具有固定线
 * 程数的线程池）。然后，定义了一个 Callable 对象作为任务，该任务会将1到100,000之间的
 * 数字相加，并返回结果。接下来，我们创建了一个 Future 对象数组，用于保存每个子任务的执行
 * 结果。然后，通过循环提交任务到线程池，并将返回的 Future 对象存储在数组中。最后，我们遍历
 * Future 对象数组，使用 get() 方法获取每个子任务的结果，并将其累加到总结果中。最终，输出总结果。
 */
public class ParallelCalculation {
    public static void main(String[] args) {
        int numOfThreads = 4; // 线程数

        // 创建线程池
        ExecutorService executor = Executors.newFixedThreadPool(numOfThreads);

        // 定义任务
        Callable<Long> task = () -> {
            long result = 0;
            for (long i = 1; i <= 100000; i++) {
                result += i;
            }
            return result;
        };

        // 创建 Future 对象列表
        Future<Long>[] futures = new Future[numOfThreads];

        // 提交任务
        for (int i = 0; i < numOfThreads; i++) {
            futures[i] = executor.submit(task);
        }

        // 计算总结果
        long totalResult = 0;
        for (int i = 0; i < numOfThreads; i++) {
            try {
                totalResult += futures[i].get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        // 输出总结果
        System.out.println("总结果：" + totalResult);

        // 关闭线程池
        executor.shutdown();
    }
}

