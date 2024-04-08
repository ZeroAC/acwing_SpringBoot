package com.kob.backend.study.myThread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 通常来说，我们使用Runnable和Thread来创建一个新的线程。但是它们有一个弊端，
 * 就是run方法是没有返回值的。而有时候我们希望开启一个线程去执行一个任务，并且
 * 这个任务执行完成后有一个返回值。
 * JDK提供了Callable接口与Future接口为我们解决这个问题，这也是所谓的“异步”模型。
 */

public class CallableTest implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
        // 模拟计算需要3秒
        Thread.sleep(3000);
        return 2;
    }

    public static void main(String args[]) throws Exception {
        // 使用
        ExecutorService executor = Executors.newCachedThreadPool();
        CallableTest task = new CallableTest();
        Future<Integer> result = executor.submit(task);
        // 注意调用get方法会阻塞当前线程，直到得到结果。
        // 所以实际编码中建议使用可以设置超时时间的重载get方法。
        System.out.println(result.get());
        System.out.println("继续执行剩余...");
    }
}
