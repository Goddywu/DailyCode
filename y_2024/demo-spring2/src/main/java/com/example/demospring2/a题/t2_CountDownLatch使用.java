package com.example.demospring2.a题;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2024-06-11
 * 计数器，计数器为0停止阻塞主线程
 * 场景1: 六人会议，集齐才开会
 * 场景2: 裁判发令，一起跑
 */
public class t2_CountDownLatch使用 {

    private static class Container {
        private List<Integer> list = new ArrayList<>();

        public void add(int num) {
            list.add(num);
        }

        public int size() {
            return list.size();
        }
    }

    /**
     * 实现一个容器，提供两个方法，add，size 写两个线程，线程1添加10个元素到容器中，线程2实现监控元素的个数，当个数到5个时，线程2给出提示并结束?
     * 使用CountDownLatch 代替wait notify 好处
     */
    public static void method1() {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Container container = new Container();

        new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                container.add(i);
                System.out.println("t1添加元素 " + i);
                if (container.size() == 5) {
                    countDownLatch.countDown();
                }
            }
            System.out.println("t1结束");
        }, "t1").start();

        new Thread(() -> {
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("t2结束");
        }, "t2").start();
    }

    /**
     * 六人会议
     */
    public static void method2() {
        CountDownLatch countDownLatch = new CountDownLatch(6);
        for (int i = 0; i < 6; i++) {
            int finalI = i;
            new Thread(() -> {
                try {
                    Thread.sleep((long) Math.random() * 1000);
                    System.out.println("人员" + finalI + "已到达会议室");
                    countDownLatch.countDown();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }, "t" + i).start();
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("人到齐了，开会吧");
    }

    /**
     * 裁判发令，一起跑
     */
    public static void method3() {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        for (int i = 0; i < 6; i++) {
            int finalI = i;
            new Thread(() -> {
                System.out.println("运动员" + finalI + "准备好了");
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("运动员" + finalI + "开跑");
            }, "t" + i).start();
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("裁判发令🔫");
        countDownLatch.countDown();
    }


    public static void main(String[] args) {
//        method1();

//        method2();

        method3();
    }
}
