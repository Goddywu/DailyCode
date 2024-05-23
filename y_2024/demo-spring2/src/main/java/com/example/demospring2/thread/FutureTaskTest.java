package com.example.demospring2.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2023-12-26
 */
@Slf4j
public class FutureTaskTest {

    private final static ExecutorService executor = Executors.newFixedThreadPool(1);

    private static void test1() {
//        FutureTask<Void> task1 = new FutureTask<>(() -> {
//            log.info("task1开始");
//            TimeUnit.SECONDS.sleep(2);
//            log.info("task1结束");
//            return null;
//        });

        FutureTask<Void> task2 = new FutureTask<>(() -> {
            log.info("task2开始");
            TimeUnit.SECONDS.sleep(30);
            log.info("task2结束");
            return null;
        });

//        Future<?> submit1 = executor.submit(task1);
        Future<?> submit2 = executor.submit(task2);

        try {
//            task2.get(4, TimeUnit.SECONDS);
            submit2.get(4, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("task1超时");
//            submit2.cancel(true);
//            task2.cancel(true);
        }
        log.info("end");
        executor.shutdown();
    }

    public static void main(String[] args) {
        test1();
    }

}
