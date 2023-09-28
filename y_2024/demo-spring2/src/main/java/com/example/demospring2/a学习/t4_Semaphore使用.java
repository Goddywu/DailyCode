package com.example.demospring2.a学习;

import java.util.concurrent.Semaphore;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2024-06-11
 * semaphore可以用来控制同时访问特定资源的线程数量，通过协调各个线程，以保证合理的使用资源。
 * 举个例子，可以把它理解成我们停车场入口立着的那个显示屏，每有一辆车进入停车场显示屏就会显示剩余车位减1，每有一辆车从停车场出去，显示屏上显示的剩余车辆就会加1，当显示屏上的剩余车位为0时，车辆就无法进入停车场了，直到有一辆车从停车场出去为止。也可以看出来semaphore可以用来使用在有数量访问限制要求的场景下
 */
public class t4_Semaphore使用 {

    public static void method1() {
        Semaphore semaphore = new Semaphore(3);
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            new Thread(() -> {
                System.out.println("汽车" + finalI + "来停车场");
                try {
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("汽车" + finalI + "停车🅿️");

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                System.out.println("汽车" + finalI + "离开🏃");
                semaphore.release();
            }, "t" + i).start();
        }
    }

    public static void main(String[] args) {
        method1();
    }
}
