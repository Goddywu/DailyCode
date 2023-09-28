package 并发案例;

import cn.hutool.json.JSONUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2024-04-01
 */
public class CountDownLatch使用 {


    /*
        实现一个容器，提供两个方法，add，size
        写两个线程，线程1添加10个元素到容器中，线程2实现监控元素的个数，当个数到5个时，线程2给出提示并结束.
        https://pdai.tech/md/java/thread/java-thread-x-juc-tool-countdownlatch.html
     */


    private static class ContainerV1 {

        private final List<Integer> list = new ArrayList<>();

        public void add(int i) {
            list.add(i);
        }

        public int size() {
            return list.size();
        }

        public static void main(String[] args) {
            ContainerV1 v1 = new ContainerV1();
            Thread t1 = new Thread(() -> {
                System.out.println("线程1启动");
                synchronized (v1) {
                    for (int i = 0; i < 10; i++) {
                        v1.add(new Random().nextInt(10));
                        System.out.println("线程1  v1: " + v1.size());
                        if (i < 5) {
                            v1.notify();
                            try {
                                v1.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                System.out.println("线程1结束");
            }, "线程1");

            Thread t2 = new Thread(() -> {
                System.out.println("线程2启动");
                synchronized (v1) {
                    while (v1.size() < 5) {
                        v1.notify();
                        try {
                            v1.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    v1.notify();
                }
                System.out.println("线程2结束");
            }, "线程2");

            t2.start();
            t1.start();
        }
    }

    private static class ContainerV2 {
        private final List<Integer> list = new ArrayList<>();

        public void add(int i) {
            list.add(i);
        }

        public int size() {
            return list.size();
        }

        public static void main(String[] args) {
            ContainerV2 v2 = new ContainerV2();
            CountDownLatch countDownLatch = new CountDownLatch(1);

            Thread t1 = new Thread(() -> {
                System.out.println("t1 start");
                for (int i = 0; i < 10; i++) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    v2.add(i);
                    System.out.println("[t1] v2: " + v2.size());
                    if (i == 4) {
                        countDownLatch.countDown();
                    }
                }
                System.out.println("t1 end");
            });

            Thread t2 = new Thread(() -> {
                System.out.println("t2 start");
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("t2 end");
            });

            t2.start();
            t1.start();
        }

    }

}
