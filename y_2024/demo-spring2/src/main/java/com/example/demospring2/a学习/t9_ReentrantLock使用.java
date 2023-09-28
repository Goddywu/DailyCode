package com.example.demospring2.a学习;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2024-06-12
 * https://pdai.tech/md/java/thread/java-thread-x-lock-ReentrantLock.html
 */
public class t9_ReentrantLock使用 {

    private static void method1() {
        ReentrantLock lock = new ReentrantLock();
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                long sleepMs = (long) (Math.random() * 1000);
                System.out.println(Thread.currentThread().getName() + "开始，睡" + sleepMs + "ms");
                lock.lock();
                try {
                    Thread.sleep(sleepMs);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    lock.unlock();
                }
                System.out.println(Thread.currentThread().getName() + "结束");
            }, "t" + i).start();
        }
    }

    private static class Counter {
        private static int counter = 0;
        private static ReentrantLock lock = new ReentrantLock();

        public static int getCounter() {
            return counter;
        }

        public static void increase() {
            try {
                lock.lock();
                counter++;
            } finally {
                lock.unlock();
            }
        }
    }

    private static void method2() {


    }

    public static void main(String[] args) {
        method1();

        method2();
    }
}
