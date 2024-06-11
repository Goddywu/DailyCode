package org.example.interview2024.misc;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2024-06-07
 */
@Slf4j
public class 美团_线程交替打印 {

    public static void method1() {
        AtomicInteger counter = new AtomicInteger();
        new Thread(() -> {
            while (true) {
                if (counter.get() % 3 == 0 && counter.get() < 10) {
                    log.info(Thread.currentThread().getName());
                    counter.incrementAndGet();
                }
            }
        }, "t1").start();

        new Thread(() -> {
            while (true) {
                if (counter.get() % 3 == 1 && counter.get() < 10) {
                    log.info(Thread.currentThread().getName());
                    counter.incrementAndGet();
                }
            }
        }, "t2").start();
        new Thread(() -> {
            while (true) {
                if (counter.get() % 3 == 2 && counter.get() < 10) {
                    log.info(Thread.currentThread().getName());
                    counter.incrementAndGet();
                }
            }
        }, "t3").start();
    }

    public static void main(String[] args) {
        method1();
    }
}
