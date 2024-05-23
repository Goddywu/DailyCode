package com.example.demospring2.c停止任务;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2023-10-17
 */
@Slf4j
public class Test1 {

    private final static ThreadPoolExecutor executor = new ThreadPoolExecutor(
            1, 1, 10,
            TimeUnit.MINUTES, new LinkedBlockingDeque<>());
    private final static ConcurrentMap<String, FutureTask<Integer>> taskMap = new ConcurrentHashMap<>();

    public static void test1() throws InterruptedException {

        FutureTask<Integer> task1 = new FutureTask<>(() -> {
            System.out.println("任务1开始执行");
            Thread.sleep(1_000);
            System.out.println("任务1执行完毕");
            return 1;
        });
        taskMap.put("1", task1);
        executor.submit(task1);

        FutureTask<Integer> task2 = new FutureTask<>(() -> {
            System.out.println("任务2开始执行");
            Thread.sleep(1_000);
            System.out.println("任务2执行完毕");
            return 2;
        });
        taskMap.put("2", task2);
        executor.submit(task2);

        Thread.sleep(1000);


        FutureTask<Integer> myTask = taskMap.get("1");
        if (myTask.isDone()) {
            System.out.println("任务1已经结束了");
        } else {
            myTask.cancel(true);
            System.out.println("任务1终止了");
        }

        executor.shutdown();
    }

    public static void test2() {
        log.info("=================== {} ===================\n{}\n\n\n", "line1", "line2");
        log.info("=================== {} ===================\n{}\n\n\n", "line1", "line2");
    }

    @Data
    @AllArgsConstructor
    private static class CpInfo {
        private String cpName;
        private int problemNum;
        private long accept;
    }

    @Data
    private static class ShuntGroupConfig {
        private Boolean isFull;

        public boolean isFull() {
            return isFull != null && isFull;
        }
    }


    public static void main(String[] args) throws Exception {
//        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
//        for (int i = 2; i < stackTrace.length; i++) {
//            StackTraceElement element = stackTrace[i];
//            System.out.println("\tat " + element.getClassName() + "." + element.getMethodName()
//                    + "(" + element.getFileName() + ":" + element.getLineNumber() + ")");
//        }
//        List<CpInfo> integers = Arrays.asList(
//                new CpInfo("1", 3, 1),
//
//                new CpInfo("2", 2, 1),
//
//                new CpInfo("3", 2, 2)
//        );
//        integers.sort(Comparator.comparingInt(CpInfo::getProblemNum).thenComparingLong(CpInfo::getAccept).reversed());
//        System.out.println(integers);

        ShuntGroupConfig config = new ShuntGroupConfig();
        if (config.isFull()) {
            System.out.println(111);
        }

//        test2();

//            String s = "select count(1) from eagle_ws_notice where (start_time <= #{endTime} and end_time >= #{startTime}) or (start_time >= #{startTime} and end_time <= #{endTime}) or (start_time <= #{startTime} and end_time >= #{endTime})";
//            System.out.println(s.replaceAll("#\\{startTime\\}", "\"2023-10-28 00:00:00\"").replaceAll("#\\{endTime\\}", "\"2023-10-29 00:00:00\""));
    }
}
