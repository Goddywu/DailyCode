package com.example.demospring2.a学习;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2024-06-12
 */
public class t7_线程使用 {

    ////////////////////// 使用线程 /////////////////////////////////////

    // 使用线程，方法1: 实现 Runnable
    private static class MyRunnable implements Runnable {
        @Override
        public void run() {
            System.out.println("执行MyRunnable！");
        }
    }

    // 使用线程，方法2: 实现 Callable，对比Runnable会有返回值
    private static class MyCallable implements Callable<String> {
        @Override
        public String call() throws Exception {
            System.out.println("执行MyCallable！");
            return "hi";
        }
    }

    // 使用线程，方法3: 集成 Thread
    private static class MyThread extends Thread {
        @Override
        public void run() {
            System.out.println("执行MyThread！");
        }
    }
    
    
    ////////////////////// 创建线程 /////////////////////////////////////

    private static void method1() {
        // 一个任务创建一个线程
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        // 所有任务只能使用固定大小的线程
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(2);
        // 相当于大小为1的fixedThreadPool
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();


    }


    public static void main(String[] args) {

    }
}
