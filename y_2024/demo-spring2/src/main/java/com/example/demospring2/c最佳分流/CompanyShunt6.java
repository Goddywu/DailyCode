package com.example.demospring2.c最佳分流;

import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2023-02-20
 */
@Slf4j
@Data
public class CompanyShunt6 {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class CompanyGroup {

        private Long companyGroupId; // 公司组id，和companyId二选一
        private Long companyId; // 公司id，和companyId二选一
        private int companyNum; // 公司总数
        private int bossNum; // 老板总数
        private int chuFaBossNum; // 触发老板总数
        private int payBossNum; // 付费老板总数
        private int moneyNum;

        private String group;
    }

    public static List<CompanyGroup> syncFindBest(
            List<CompanyGroup> companyGroupList,
            double percent,
            int allowBossNumBias
    ) {
        int totalSize = companyGroupList.size();
        int maxPieceSize = 1_0000;
        long startMs = System.currentTimeMillis();

        int totalBossNum = 0;
        int totalChuFaBossNum = 0;
        int totalPayBossNum = 0;
        int totalMoney = 0;
        for (CompanyGroup companyGroup : companyGroupList) {
            totalBossNum += companyGroup.getBossNum();
            totalChuFaBossNum += companyGroup.getChuFaBossNum();
            totalPayBossNum += companyGroup.getPayBossNum();
            totalMoney += companyGroup.getMoneyNum();
        }
        double bestArpu = (double) totalMoney / totalChuFaBossNum;
        double bestPay = (double) totalPayBossNum / totalChuFaBossNum;
        double bestArppu = (double) totalMoney / totalPayBossNum;

        List<CompanyGroup> bestCompanyGroup = new ArrayList<>();
        List<Pair<Integer, Integer>> boundaryList = getBoundary(totalSize, maxPieceSize);
        for (int i = 0; i < boundaryList.size(); i++) {
            Pair<Integer, Integer> boundary = boundaryList.get(i);
            log.info("--------开始执行第{}组，总共{}组------------", i + 1, boundaryList.size());
            List<CompanyGroup> subCompanyGroupList = companyGroupList.subList(boundary.getKey(), boundary.getValue());
            List<CompanyGroup> subBest = findSubBest(subCompanyGroupList, percent, allowBossNumBias, bestArpu, bestPay, bestArppu);
            if (subBest != null) {
                bestCompanyGroup.addAll(subBest);
            }
        }
        log.info("--------------------");
        log.info("【总】耗时{}ms", (System.currentTimeMillis() - startMs));
        return bestCompanyGroup;
    }

    public static List<CompanyGroup> asyncFindBest(
            List<CompanyGroup> companyGroupList,
            double percent,
            int allowBossNumBias
    ) {
        int totalSize = companyGroupList.size();
        int maxPieceSize = 8_0000;
        long startMs = System.currentTimeMillis();
        ExecutorService executor = Executors.newFixedThreadPool(2);

        int totalBossNum = 0;
        int totalChuFaBossNum = 0;
        int totalPayBossNum = 0;
        int totalMoney = 0;
        for (CompanyGroup companyGroup : companyGroupList) {
            totalBossNum += companyGroup.getBossNum();
            totalChuFaBossNum += companyGroup.getChuFaBossNum();
            totalPayBossNum += companyGroup.getPayBossNum();
            totalMoney += companyGroup.getMoneyNum();
        }
        double bestArpu = (double) totalMoney / totalChuFaBossNum;
        double bestPay = (double) totalPayBossNum / totalChuFaBossNum;
        double bestArppu = (double) totalMoney / totalPayBossNum;

        Vector<CompanyGroup> bestCompanyGroup = new Vector<>();
        List<Pair<Integer, Integer>> boundaryList = getBoundary(totalSize, maxPieceSize);
        List<CompletableFuture<Void>> futureList = new ArrayList<>();
        for (int i = 0; i < boundaryList.size(); i++) {
            final int finalI = i;
            futureList.add(CompletableFuture.runAsync(() -> {
                Pair<Integer, Integer> boundary = boundaryList.get(finalI);
                log.info("--------开始执行第{}组，总共{}组------------", finalI + 1, boundaryList.size());
                List<CompanyGroup> subCompanyGroupList = companyGroupList.subList(boundary.getKey(), boundary.getValue());
                List<CompanyGroup> subBest = findSubBest(subCompanyGroupList, percent, allowBossNumBias, bestArpu, bestPay, bestArppu);
                if (subBest != null) {
                    bestCompanyGroup.addAll(subBest);
                }
            }, executor));
        }
        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0])).join();
        log.info("--------------------");
        log.info("【总】耗时{}ms", (System.currentTimeMillis() - startMs));
        executor.shutdown();
        return bestCompanyGroup;
    }

    private static List<CompanyGroup> findSubBest(
            List<CompanyGroup> companyGroupList,
            double percent,
            int allowBossNumBias,
            double bestArpu,
            double bestPay,
            double bestArppu
    ) {
        // 统计获取预期值
        int totalBossNum = 0; // 统计下boss总人数
        for (CompanyGroup companyGroup : companyGroupList) {
            totalBossNum += companyGroup.getBossNum();
        }
        int bestBossNum = (int) Math.ceil(totalBossNum * percent);
        int minBossNum = bestBossNum - allowBossNumBias;
        int maxBossNum = bestBossNum + allowBossNumBias;
        log.info("一共{}个公司组，最佳分配{}个老板", companyGroupList.size(), bestBossNum);

        // companyGroupList.size() + 1, bestBossNum + 1
        List<BitSet> dp = buildDp(companyGroupList, bestBossNum);
        if (!dp.get(companyGroupList.size()).get(bestBossNum)) {
            log.error("没有组合可以构成boss总数{}", bestBossNum);
            return null;
        }

        List<CompanyGroup> bestCompanyGroup = findBestCompanyGroup(companyGroupList, dp, bestBossNum, bestArpu, bestPay, bestArppu);

        return bestCompanyGroup;
    }

    private static List<CompanyGroup> findBestCompanyGroup(
            List<CompanyGroup> companyGroupList,
            List<BitSet> dp,
            int bestBossNum,
            double bestArpu,
            double bestPay,
            double bestArppu
    ) {
        long startMs = System.currentTimeMillis();
        int combinationCount = 0;
        int updateBestCount = 0;

        Double betterScore = null;
        Double betterArpu = null;
        Double betterPay = null;
        Double betterArppu = null;
        List<CompanyGroup> betterCompanyGroup = null;

        for (int n = companyGroupList.size(); n >= 0; n--) {
            List<CompanyGroup> combination = new ArrayList<>();
            int remainingSum = bestBossNum;
            for (int i = n; i > 0 && remainingSum > 0; i--) {
                if (remainingSum >= companyGroupList.get(i - 1).getBossNum() && dp.get(i - 1).get(remainingSum - companyGroupList.get(i - 1).getBossNum()) && (i == 1 || companyGroupList.get(i - 2).getBossNum() != companyGroupList.get(i - 1).getBossNum())) {
                    combination.add(companyGroupList.get(i - 1));
                    remainingSum -= companyGroupList.get(i - 1).getBossNum();
                }
            }
            if (!combination.isEmpty()) {
                int totalChuFaBossNum = 0;
                int totalPayBossNum = 0;
                int totalMoney = 0;
                for (CompanyGroup companyGroup : combination) {
                    totalChuFaBossNum += companyGroup.getChuFaBossNum();
                    totalPayBossNum += companyGroup.getPayBossNum();
                    totalMoney += companyGroup.getMoneyNum();
                }
                double arpu = (double) totalMoney / totalChuFaBossNum;
                double pay = (double) totalPayBossNum / totalChuFaBossNum;
                double arppu = (double) totalMoney / totalPayBossNum;
                double score = calcScore(bestArpu, arpu, bestPay, pay, bestArppu, arppu);

                if (betterScore == null || score > betterScore) {
                    betterScore = score;
                    betterCompanyGroup = combination;
                    betterArpu = arpu;
                    betterPay = pay;
                    betterArppu = arppu;

                    updateBestCount++;
                }
                combinationCount++;
            }
        }
        log.info("采用组合{}个，更新最佳{}次，耗时{}ms", combinationCount, updateBestCount, (System.currentTimeMillis() - startMs));
        log.info("[arpu] 最佳{}，当前{}", bestArpu, betterArpu);
        log.info("[pay] 最佳{}，当前{}", bestPay, betterPay);
        log.info("[arppu] 最佳{}，当前{}", bestArppu, betterArppu);
        return betterCompanyGroup;
    }

    private static double calcScore(
            double bestArpu, double arpu,
            double bestPay, double pay,
            double bestArppu, double arppu
    ) {
        return -(Math.pow(bestArpu - arpu, 2) + Math.pow(bestPay - pay, 2) + Math.pow(bestArppu - arppu, 2));
    }

    private static List<BitSet> buildDp(
            List<CompanyGroup> companyGroupList,
            int bestBossNum
    ) {
        long startMs = System.currentTimeMillis();
        int len = companyGroupList.size();

        // 初始化dp数组
        List<BitSet> dp = new ArrayList<>(len + 1);
        for (int i = 0; i <= len + 1; i++) {
            BitSet bitSet = new BitSet(bestBossNum + 1);
            dp.add(bitSet);
        }
        dp.get(0).set(0, true);
        for (int i = 1; i <= len; i++) {
            dp.get(i).set(0, true);
        }

        // 填充dp数组
        for (int i = 1; i <= len; i++) {
            for (int j = 1; j <= bestBossNum; j++) {
                if (j < companyGroupList.get(i - 1).getBossNum()) {
                    dp.get(i).set(j, dp.get(i - 1).get(j));
                } else {
                    boolean bool = dp.get(i - 1).get(j) || (i == 1 || companyGroupList.get(i - 2).getBossNum() != companyGroupList.get(i - 1).getBossNum()) && dp.get(i - 1).get(j - companyGroupList.get(i - 1).getBossNum());
                    dp.get(i).set(j, bool);
                }
            }
        }
        log.info("构建dp，耗时{}ms", (System.currentTimeMillis() - startMs));
        return dp;
    }

    private static List<Pair<Integer, Integer>> getBoundary(int totalSize, int maxPieceSize) {
        List<Pair<Integer, Integer>> boundaries = new ArrayList<>();
        if (totalSize <= maxPieceSize) {
            return Collections.singletonList(Pair.of(0, totalSize));
        }

        int divideNum = (int) Math.ceil((double) totalSize / maxPieceSize);
        int onePieceNum = Math.floorDiv(totalSize, divideNum);
        int left = totalSize % divideNum;
        int from = 0;
        int to = onePieceNum;
        if (left > 0) {
            to++;
            left--;
        }
        for (int i = 0; i < divideNum; i++) {
            boundaries.add(Pair.of(from, to));

            from = to;
            to = Math.min(onePieceNum + to, totalSize);
            if (i != divideNum - 1 && left > 0) {
                to++;
                left--;
            }
        }
        return boundaries;
    }

    private static long parseLong(String s) {
        if (StrUtil.isBlank(s) || "NULL".equals(s)) {
            return 0L;
        }
        return Long.parseLong(s);
    }

    private static int parseInt(String s) {
        if (StrUtil.isBlank(s) || "NULL".equals(s)) {
            return 0;
        }
        return Integer.parseInt(s);
    }

    public static void main(String[] args) {
        // 配置项
        int maxCalcSecond = 60; // 程序运行时间
        double percent = 0.3; // 占比
        int allowBossNumBias = 20; // 允许的老板人数的偏差


        // =================================================================


        // 模拟数据
//        long startMs = System.currentTimeMillis();
//        List<CompanyGroup> companyGroupList = new ArrayList<>();
//        Random random = new Random();
//        for (int i = 0; i < 10_0000; i++) {
//            int randomBossNum = random.nextInt(1000);
//            int randomCompanyNum = random.nextInt(10);
//            int randomChuFaBossNum = random.nextInt(20);
//            int randomPayBossNum = random.nextInt(20);
//            int randomMoneyNum = random.nextInt(1000);
//            companyGroupList.add(new CompanyGroup(i, randomCompanyNum, randomBossNum, randomChuFaBossNum, randomPayBossNum, randomMoneyNum));
//        }
//        log.info("获取数据完毕，耗时{}ms", (System.currentTimeMillis() - startMs));
//
////        syncFindBest(companyGroupList, percent, allowBossNumBias);
//
//        asyncFindBest(companyGroupList, percent, allowBossNumBias);
//
//        log.info("总共耗时{}ms", (System.currentTimeMillis() - startMs));


        /**

         async 2线程 10_0000公司组 1000老板
         总共耗时397653ms = 397s = 6.6min

         sync 1线程 10_0000公司组 1000老板
         总共耗时647293ms = 647s = 10.7min

         */


        // =================================================================


//        List<CompanyGroup> companyGroupList = new ArrayList<>();
//        String file = "/Users/admin/Desktop/sample_excel.csv";
//        String line;
//        String[] lineArr;
//        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
//            while ((line = br.readLine()) != null) {
//                lineArr = line.split(",");
//                CompanyGroup companyGroup = new CompanyGroup();
//                companyGroup.setCompanyGroupId(Integer.parseInt(lineArr[0]));
//                companyGroup.setCompanyNum(Integer.parseInt(lineArr[1]));
//                companyGroup.setBossNum(Integer.parseInt(lineArr[2]));
//                companyGroup.setChuFaBossNum(Integer.parseInt(lineArr[3]));
//                companyGroup.setPayBossNum(Integer.parseInt(lineArr[4]));
//                companyGroup.setMoneyNum(Integer.parseInt(lineArr[5]));
//
//                companyGroupList.add(companyGroup);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        log.info("获取数据完毕");
//        findBest(companyGroupList, percent, allowBossNumBias);


        // =================================================================


        List<CompanyGroup> companyGroupList = new ArrayList<>();

        String file1 = "/Users/admin/Desktop/有关联公司.csv";
        String line;
        String[] lineArr;
        try (BufferedReader br = new BufferedReader(new FileReader(file1))) {
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                lineArr = line.split(",");
                CompanyGroup companyGroup = new CompanyGroup();
                companyGroup.setCompanyGroupId(parseLong(lineArr[0]));
                companyGroup.setCompanyNum(parseInt(lineArr[1]));
                companyGroup.setBossNum(parseInt(lineArr[2]));
                companyGroup.setChuFaBossNum(parseInt(lineArr[3]));
                companyGroup.setPayBossNum(parseInt(lineArr[4]));
                companyGroup.setMoneyNum(parseInt(lineArr[5]));

                companyGroupList.add(companyGroup);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String file2 = "/Users/admin/Desktop/无关联公司.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(file2))) {
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                lineArr = line.split(",");
                CompanyGroup companyGroup = new CompanyGroup();
                companyGroup.setCompanyId(parseLong(lineArr[0]));
                companyGroup.setCompanyNum(parseInt(lineArr[1]));
                companyGroup.setBossNum(parseInt(lineArr[2]));
                companyGroup.setChuFaBossNum(parseInt(lineArr[3]));
                companyGroup.setPayBossNum(parseInt(lineArr[4]));
                companyGroup.setMoneyNum(parseInt(lineArr[5]));

                companyGroupList.add(companyGroup);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Collections.shuffle(companyGroupList);
        log.info("获取数据完毕");

        List<CompanyGroup> companyGroups = asyncFindBest(companyGroupList, percent, allowBossNumBias);

        companyGroups.forEach(companyGroup -> {
            companyGroup.setGroup("A");
        });
        companyGroupList.forEach(companyGroup -> {
            if (companyGroup.getGroup() == null) {
                companyGroup.setGroup("B");
            }
        });

        long startMs = System.currentTimeMillis();
        String fileName = "/Users/admin/Desktop/output.csv";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            bw.append(String.join(",", Arrays.asList("group_id", "com_id", "group")));
            for (CompanyGroup companyGroup : companyGroupList) {
                bw.newLine();
                bw.append(String.join(",", Arrays.asList(
                        companyGroup.getCompanyGroupId() + "",
                        companyGroup.getCompanyId() + "",
                        companyGroup.getGroup() + ""
                )));
            }
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("写数据文件耗时{}ms", (System.currentTimeMillis() - startMs));

        int totalBossNum = 0;
        int totalChuFaBossNum = 0;
        int totalPayBossNum = 0;
        int totalMoney = 0;
        int A_totalBossNum = 0;
        int A_totalChuFaBossNum = 0;
        int A_totalPayBossNum = 0;
        int A_totalMoney = 0;
        int B_totalBossNum = 0;
        int B_totalChuFaBossNum = 0;
        int B_totalPayBossNum = 0;
        int B_totalMoney = 0;

        for (CompanyGroup companyGroup : companyGroupList) {
            if ("A".equals(companyGroup.getGroup())) {
                A_totalBossNum += companyGroup.getBossNum();
                A_totalChuFaBossNum += companyGroup.getChuFaBossNum();
                A_totalPayBossNum += companyGroup.getPayBossNum();
                A_totalMoney += companyGroup.getMoneyNum();
            } else if ("B".equals(companyGroup.getGroup())) {
                B_totalBossNum += companyGroup.getBossNum();
                B_totalChuFaBossNum += companyGroup.getChuFaBossNum();
                B_totalPayBossNum += companyGroup.getPayBossNum();
                B_totalMoney += companyGroup.getMoneyNum();
            } else {
                log.error("没分组" + companyGroup.toString());
            }

            totalBossNum += companyGroup.getBossNum();
            totalChuFaBossNum += companyGroup.getChuFaBossNum();
            totalPayBossNum += companyGroup.getPayBossNum();
            totalMoney += companyGroup.getMoneyNum();
        }

        double arpu = (double) totalMoney / totalChuFaBossNum;
        double pay = (double) totalPayBossNum / totalChuFaBossNum;
        double arppu = (double) totalMoney / totalPayBossNum;
        double A_arpu = (double) A_totalMoney / A_totalChuFaBossNum;
        double A_pay = (double) A_totalPayBossNum / A_totalChuFaBossNum;
        double A_arppu = (double) A_totalMoney / A_totalPayBossNum;
        double B_arpu = (double) B_totalMoney / B_totalChuFaBossNum;
        double B_pay = (double) B_totalPayBossNum / B_totalChuFaBossNum;
        double B_arppu = (double) B_totalMoney / B_totalPayBossNum;

        String fileName2 = "/Users/admin/Desktop/output2.txt";

        List<String> textList = Arrays.asList(
                "--------总---------",
                "总老板数：" + totalBossNum,
                "总触发老板数：" + totalChuFaBossNum,
                "总付费老板数：" + totalPayBossNum,
                "总消费金额：" + totalMoney,
                "总arpu：" + arpu,
                "总付费率：" + pay,
                "总arppu：" + arppu,

                "--------A组---------",
                "A组总老板数：" + A_totalBossNum,
                "A组触发老板数：" + A_totalChuFaBossNum,
                "A组付费老板数：" + A_totalPayBossNum,
                "A组消费金额：" + A_totalMoney,
                "A组arpu：" + A_arpu,
                "A组付费率：" + A_pay,
                "A组arppu：" + A_arppu,

                "--------B组---------",
                "B组总老板数：" + B_totalBossNum,
                "B组触发老板数：" + B_totalChuFaBossNum,
                "B组付费老板数：" + B_totalPayBossNum,
                "B组消费金额：" + B_totalMoney,
                "B组arpu：" + B_arpu,
                "B组付费率：" + B_pay,
                "B组arppu：" + B_arppu
        );

        startMs = System.currentTimeMillis();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName2))) {
            bw.append(" ");
            for (String text : textList) {
                bw.newLine();
                bw.append(text);
            }

            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("写统计文件耗时{}ms", (System.currentTimeMillis() - startMs));
    }
}
