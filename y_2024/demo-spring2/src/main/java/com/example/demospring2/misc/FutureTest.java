package com.example.demospring2.misc;

import java.util.concurrent.*;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2023-09-01
 */
public class FutureTest {

    public static void test1() throws Exception {
        ThreadPoolExecutor pool = new ThreadPoolExecutor(1, 1, 1, TimeUnit.MILLISECONDS, new SynchronousQueue<>());
        FutureTask<String> task = new FutureTask<String>(
                // 这个任务  就是你的接口方法内容
                new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        Thread.sleep(100);
                        return "aaa";
                    }
                });
        final Future<?> f = pool.submit(task);
        try {
            String unused = task.get(200, TimeUnit.MILLISECONDS);
            System.out.println(unused);
        }catch (InterruptedException | ExecutionException | TimeoutException e){
            // 捕获异常  1 interrupt是被打断 2 其他任务内的异常都是execution异常 3 超时异常，get导致的
            // 主动cancel 可以让线程池回收线程
            System.out.println("time out");
            f.cancel(true);
        }
        Thread.sleep(100);
        System.out.println("ok");

        pool.shutdown();
    }

    public static void main(String[] args) throws Exception {
        test1();
    }
}
