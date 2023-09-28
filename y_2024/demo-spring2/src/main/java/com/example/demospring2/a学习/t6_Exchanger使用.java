package com.example.demospring2.a学习;

import java.util.concurrent.Exchanger;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2024-06-11
 * 有且只有两个线程交换数据
 */
public class t6_Exchanger使用 {

    public static void method1() {
        Exchanger<String> exchanger = new Exchanger<>();
        new Thread(() -> {
            String thing = "20块钱";
            System.out.println("t1有:" + thing);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try {
                String exchange = exchanger.exchange(thing);
                System.out.println("t1交换完有：" + exchange);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }, "t1").start();

        new Thread(() -> {
            String thing = "炸鸡";
            System.out.println("t2有:" + thing);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            try {
                String exchange = exchanger.exchange(thing);
                System.out.println("t2交换完有：" + exchange);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }, "t2").start();
    }


    public static void main(String[] args) {
        method1();


    }
}
