package com.example.demospring2.a学习;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2024-06-11
 */
public class t1_notifyAndWait {

    /**
     * wait notify
     */
    public static void test1() {
        Object lock = new Object();

        new Thread(() -> {
            synchronized (lock) {
                System.out.println("t1 开始 ");
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("t1 结束");
            }
        }, "t1").start();

        new Thread(() -> {
            synchronized (lock) {
                System.out.println("t2 开始");
                lock.notify();
                System.out.println("t2 结束");
            }
        }, "t2").start();
    }

    /**
     * 交替打印
     */
    public static void test2() {
        Object lock = new Object();
        AtomicInteger counter = new AtomicInteger();

        new Thread(() -> {
            synchronized (lock) {
                while (counter.get() < 10) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println(counter.getAndIncrement() + "： t1打印");
                    lock.notify();
                }
            }
        }, "t1").start();

        new Thread(() -> {
            synchronized (lock) {
                while (counter.get() < 10) {
                    lock.notify();
                    System.out.println(counter.getAndIncrement() + ": t2打印");
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }, "t2").start();
    }


    public static void main(String[] args) {
//        System.out.println("-- test1 --");
//        test1();

        System.out.println("-- test2 --");
        test2();
    }

}
