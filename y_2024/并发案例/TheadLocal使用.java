package 并发案例;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2024-04-02
 */
public class TheadLocal使用 {

    private static class MyTest {
        static ThreadLocal<Integer> threadLocal = new ThreadLocal<>();

        public static void main(String[] args) {
            threadLocal.set(1);
        }
    }
}
