package 并发案例;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2024-04-02
 */
public class 单例模式 {

    // 饿汉式
    public static class M1 {
        private static final M1 instance = new M1();

        public static M1 getInstance() {
            return instance;
        }
    }


    // dcl
    public static class M10 {
        private static M10 instance;

        public static M10 getInstance() {
            if (instance == null) {         // check
                synchronized (M10.class) {  // lock
                    if (instance == null) { // check
                        instance = new M10();
                    }
                }
            }
            return instance;
        }
    }
}
