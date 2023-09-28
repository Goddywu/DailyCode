package 并发案例;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2024-03-25
 */
public class Synchronized使用 {

    ////////////////////////// 对象锁 ///////////////////////////////////

    public static class C1 implements Runnable {
        private static C1 instance = new C1();

        @Override
        public void run() {
            synchronized (this) {
                System.out.println("线程 " + Thread.currentThread().getName() + " 开始");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + " 结束");
            }
        }

        public static void main(String[] args) {
            Thread t1 = new Thread(instance);
            Thread t2 = new Thread(instance);
            t1.start();
            t2.start();
        }
    }

    public static class C2 implements Runnable {
        private static C2 instance = new C2();

        Object block1 = new Object();
        Object block2 = new Object();

        @Override
        public void run() {
            synchronized (block1) {
                System.out.println("block1,线程 " + Thread.currentThread().getName() + " 开始");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("block1," + Thread.currentThread().getName() + " 结束");
            }
            synchronized (block2) {
                System.out.println("block2,线程 " + Thread.currentThread().getName() + " 开始");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("block2," + Thread.currentThread().getName() + " 结束");
            }
        }

        public static void main(String[] args) {
            Thread t1 = new Thread(instance);
            Thread t2 = new Thread(instance);
            t1.start();
            t2.start();
        }
    }


    ////////////////////////// 方法锁 ///////////////////////////////////

    public static class C3 implements Runnable {
        private static C3 instance = new C3();

        @Override
        public void run() {
            method();
        }

        public synchronized void method() {
            System.out.println("线程" + Thread.currentThread().getName());
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "结束");
        }

        public static void main(String[] args) {
            Thread t1 = new Thread(instance);
            Thread t2 = new Thread(instance);
            t1.start();
            t2.start();
        }
    }

    public static class C4 implements Runnable {
        private static C4 instance1 = new C4();
        private static C4 instance2 = new C4();

        @Override
        public void run() {
            method();
        }

        public synchronized void method() {
            System.out.println("线程" + Thread.currentThread().getName());
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "结束");
        }

        public static void main(String[] args) {
            Thread t1 = new Thread(instance1);
            Thread t2 = new Thread(instance2);
            t1.start();
            t2.start();
            // 不会串行，会并发
        }
    }

    public static class C5 implements Runnable {
        private static C5 instance1 = new C5();
        private static C5 instance2 = new C5();

        @Override
        public void run() {
            method();
        }

        public synchronized static void method() {
            System.out.println("线程" + Thread.currentThread().getName());
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "结束");
        }

        public static void main(String[] args) {
            Thread t1 = new Thread(instance1);
            Thread t2 = new Thread(instance2);
            t1.start();
            t2.start();
            // 锁静态方法，会串行
        }
    }

    ////////////////////////// 类锁 ///////////////////////////////////

    public static class C6 implements Runnable {
        private static C6 instance1 = new C6();
        private static C6 instance2 = new C6();

        @Override
        public void run() {
            synchronized (C6.class) {
                System.out.println("线程" + Thread.currentThread().getName());
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "结束");
            }
        }

        public static void main(String[] args) {
            Thread t1 = new Thread(instance1);
            Thread t2 = new Thread(instance2);
            t1.start();
            t2.start();
        }
    }
}
