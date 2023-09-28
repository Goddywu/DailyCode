package com.example.demospring2.a学习;

import java.util.concurrent.locks.LockSupport;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2024-06-12
 * https://pdai.tech/md/java/thread/java-thread-x-lock-LockSupport.html
 */
public class t8_LockSupport使用 {

    private static void method1() {
        Thread t1 = new Thread(() -> {
            LockSupport.park();
            System.out.println("t1");
        }, "t1");

        Thread t2 = new Thread(() -> {
            LockSupport.park();
            System.out.println("t2");
            LockSupport.unpark(t1);
        }, "t2");

        Thread t3 = new Thread(() -> {
            LockSupport.park();
            System.out.println("t3");
            LockSupport.unpark(t2);
        }, "t3");

        t1.start();
        t2.start();
        t3.start();

        LockSupport.unpark(t3);
    }


    public static void main(String[] args) {
        method1();
    }

}
