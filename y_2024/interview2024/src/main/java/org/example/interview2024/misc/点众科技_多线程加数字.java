package org.example.interview2024.misc;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2024-06-06
 */
@Slf4j
public class 点众科技_多线程加数字 {


    /*
    10个线程，
    公共变量，
    加1w次，加到10w    输出
     */

    private static class Container {
        private int val;
    }

    public static void main(String[] args) {
//        AtomicInteger val = new AtomicInteger(0);
        Object lock = new Object();
        Container c = new Container();
        List<FutureTask<Void>> futureTaskList = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i ++) {
            FutureTask<Void> task = new FutureTask<>(() -> {
                synchronized (lock) {
                    for (int j = 0; j < 10000; j++) {
                        c.val += 1;
                    }
                    return null;
                }
            });
            futureTaskList.add(task);
            executorService.submit(task);
        }

        for (FutureTask<Void> task : futureTaskList) {
            try {
                Void unused = task.get();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println(c.val);
        executorService.shutdown();

    }

}
