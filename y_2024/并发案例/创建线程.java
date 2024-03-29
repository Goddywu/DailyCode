package 并发案例;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2024-03-26
 */
public class 创建线程 {

    // 1. 继承thread
    private static class DemoThread extends Thread {
        @Override
        public void run() {
            System.out.println("线程启动");
        }

        public static void main(String[] args) {
            new DemoThread().start();
        }
    }

    // 2. 实现Runnable
    private static class DemoRunnable implements Runnable {
        @Override
        public void run() {
            System.out.println("启动线程");
        }

        public static void main(String[] args) {
            new Thread(new DemoRunnable(), "线程1").start();
        }
    }

    // 3. 实现Callable
    private static class DemoCallable implements Callable<Integer> {
        @Override
        public Integer call() throws Exception {
            System.out.println("线程启动");
            return 1;
        }

        public static void main(String[] args) throws Exception {
            FutureTask<Integer> futureTask = new FutureTask<>(new DemoCallable());
            new Thread(futureTask).start();
            Executors.newCachedThreadPool().invokeAll(new ArrayList<>());
        }
    }


    public static void main(String[] args) {

    }
}
