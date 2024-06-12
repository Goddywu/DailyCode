package com.example.demospring2.a学习;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Phaser;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2024-06-12
 * 多阶段栅栏，可以在初始时设定参与线程数，也可以中途注册/注销参与者，当到达的参与者数量满足栅栏设定的数量后，会进行阶段升级（advance）
 * https://segmentfault.com/a/1190000015979879
 */
public class t5_Phaser使用 {

    /**
     * 多个线程到达才执行
     * 类似CyclicBarrier或者CountDownLatch
     */
    public static void method1() {
        Phaser phaser = new Phaser();
        for (int i = 0; i < 10; i++) {
            phaser.register();
            int finalI = i;
            new Thread(() -> {
                try {
                    Thread.sleep(finalI * 1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                int num = phaser.arriveAndAwaitAdvance();
                System.out.println(Thread.currentThread().getName() + "执行完任务，当前num=" + num);
            }, "t" + i).start();
        }
        System.out.println("方法结束");
    }

    /**
     * 通过Phaser实现开关
     */
    public static void method2() {
        Phaser phaser = new Phaser(1); // 帮主线程注册
        for (int i = 0; i < 10; i++) {
            phaser.register();
            new Thread(() -> {
                int num = phaser.arriveAndAwaitAdvance();
                System.out.println(Thread.currentThread().getName() + "执行任务！num=" + num);
            }, "t" + i).start();
        }

        System.out.println("Press Enter to continue");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        phaser.arriveAndAwaitAdvance();
        System.out.println("主线程open开关");
    }

    /**
     * phaser控制任务执行轮数
     */
    public static void method3() {
        int repeats = 3;
        Phaser phaser = new Phaser() {
            @Override
            protected boolean onAdvance(int phase, int registeredParties) {
                System.out.println("【phase=" + phase + ", parties=" + registeredParties + "】");
                return phase + 1 >= repeats || super.onAdvance(phase, registeredParties);
            }
        };

        for (int i = 0; i < 10; i ++) {
            phaser.register();
            new Thread(() -> {
                while (!phaser.isTerminated()) {
                    int num = phaser.arriveAndAwaitAdvance();
                    System.out.println(Thread.currentThread().getName() + "执行任务 num=" + num);
                }
            }, "t" + i).start();
        }

    }

    /**
     * https://segmentfault.com/a/1190000015979879 的方法四。。
     */
    public static void method4() {

    }

    public static void method5() {
        Phaser phaser = new Phaser(2);
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                phaser.arriveAndAwaitAdvance();
                System.out.println("t1打印");
            }
        }, "t1").start();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                phaser.arriveAndAwaitAdvance();
                System.out.println("t2打印");
            }
        }, "t2").start();
    }

    public static void main(String[] args) {
//        method1();

//        method2();

//        method3();

//        method4();

        method5();
    }
}
