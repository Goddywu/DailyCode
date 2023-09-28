package com.example.demospring2.misc;

import java.util.concurrent.TimeUnit;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2023-11-09
 */
public class RegexTest {

    public static void test1() {
        String a = "com.bosszp.cola.biz.service.ShuntService.<dubbo:service beanName=\"ServiceBean:com.bosszp.cola.biz.service.ShuntService:1.0\" generic=\"false\" exported=\"true\" uniqueServiceName=\"com.bosszp.cola.biz.service.ShuntService:1.0\" unexported=\"false\" interface=\"com.bosszp.cola.biz.service.ShuntService\" path=\"com.bosszp.cola.biz.service.ShuntService\" ref=\"com.bosszp.cola.biz.api.dubbo.DefaultShutService@410ae9a3\" filter=\"dubboMockFilter\" version=\"1.0\" delay=\"10000\" timeout=\"1000\" id=\"com.bosszp.cola.biz.service.ShuntService\" />";

        System.out.println(a.replaceAll(".<.*", ""));
    }

    public static void test2() {
        Thread thread = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(i);
            }
        });
        thread.start();
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        thread.interrupt();
    }

    public static void main(String[] args) {
//        test1();
        test2();
    }
}
