package com.example.demospring2.a学习;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2024-06-11
 * countDownLatch是只能用一次，而CyclicBarrier则是可以循环使用的，这一点在上面的例子中也是体现出来了。
 * 其次，CyclicBarrier的计数器由自己控制，而CountDownLatch的计数器则由使用者来控制，在CyclicBarrier中线程调用await方法不仅会将自己阻塞还会将计数器减1，而在CountDownLatch中线程调用await方法只是将自己阻塞而不会减少计数器的值。而且CyclicBarrier也提供了更多的方法，使用起来也是更加灵活些
 */
public class t3_CyclicBarrier使用 {

    public static void method1() {
        System.out.println(Thread.currentThread().getName() + "整体开始");
        CyclicBarrier cb = new CyclicBarrier(3, () -> {
            System.out.println(Thread.currentThread().getName() + "都到了屏障");
        });

        new Thread(() -> {
            System.out.println("t1开始");
            try {
                cb.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
            System.out.println("t1结束");
        }, "t1").start();

        new Thread(() -> {
            System.out.println("t2开始");
            try {
                cb.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
            System.out.println("t2结束");
        }, "t2").start();

        try {
            cb.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (BrokenBarrierException e) {
            throw new RuntimeException(e);
        }

        System.out.println(Thread.currentThread().getName() + "整体结束");
    }

    /**
     * 收集龙珠
     */
    public static void method2() {
        CyclicBarrier cb = new CyclicBarrier(7, () -> {
            System.out.println("集齐7颗龙珠了！");
            System.out.println("神龙🐲已召唤。再等3秒可再次召唤");
        });

        for (int i = 0; i < 7; i++) {
            int finalI = i;
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + "收集第" + finalI + "颗龙珠");
                try {
                    cb.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (BrokenBarrierException e) {
                    throw new RuntimeException(e);
                }

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                System.out.println(Thread.currentThread().getName() + "收集第" + finalI + "颗龙珠");
                try {
                    cb.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (BrokenBarrierException e) {
                    throw new RuntimeException(e);
                }

            }, "t" + i).start();
        }
    }

    public static void main(String[] args) {
//        method1();

        method2();
    }
}
