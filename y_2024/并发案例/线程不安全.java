package 并发案例;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2024-03-25
 */
public class 线程不安全 {

    public static class C1 {
        private int cnt = 0;
        public void addCnt() {
            cnt++;
        }
        public int getCnt(){
            return cnt;
        }
    }

    public static void test1() throws Exception {
        ExecutorService executorService = Executors.newCachedThreadPool();
        int taskSize = 1000;
        C1 c1 = new C1();
        final CountDownLatch countDownLatch = new CountDownLatch(taskSize);
        for (int i = 0; i < taskSize; i++) {
            executorService.execute(() -> {
                c1.addCnt();
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        executorService.shutdown();
        System.out.println("cnt: " + c1.getCnt());
        // 结果会小于 1000
    }

    public static void main(String[] args) throws Exception {
        test1();
    }
}
