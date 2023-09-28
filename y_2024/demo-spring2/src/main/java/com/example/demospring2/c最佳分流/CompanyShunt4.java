package com.example.demospring2.c最佳分流;

import cn.hutool.core.lang.Pair;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sun.nio.ch.ThreadPool;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2023-02-20
 */
@Slf4j
@Data
public class CompanyShunt4 {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class CompanyGroup {

        private int companyGroupId; // 公司组id
        private int companyNum; // 公司总数
        private int bossNum; // 老板总数
        private int chuFaBossNum; // 触发老板总数
        private int payBossNum; // 付费老板总数
        private int moneyNum;
    }

    public static List<CompanyGroup> syncFindBest(
            List<CompanyGroup> companyGroupList,
            double percent,
            int allowBossNumBias
    ) {
        int totalSize = companyGroupList.size();
        int maxPieceSize = 1_0000;
        long startMs = System.currentTimeMillis();

        List<CompanyGroup> bestCompanyGroup = new ArrayList<>();
        List<Pair<Integer, Integer>> boundaryList = getBoundary(totalSize, maxPieceSize);
        for (int i = 0; i < boundaryList.size(); i++) {
            Pair<Integer, Integer> boundary = boundaryList.get(i);
            log.info("--------开始执行第{}组，总共{}组------------", i + 1, boundaryList.size());
            List<CompanyGroup> subCompanyGroupList = companyGroupList.subList(boundary.getKey(), boundary.getValue());
            List<CompanyGroup> subBest = findSubBest(subCompanyGroupList, percent, allowBossNumBias);
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
        int maxPieceSize = 1_0000;
        long startMs = System.currentTimeMillis();
        ExecutorService executor = Executors.newFixedThreadPool(2);

        Vector<CompanyGroup> bestCompanyGroup = new Vector<>();
        List<Pair<Integer, Integer>> boundaryList = getBoundary(totalSize, maxPieceSize);
        List<CompletableFuture<Void>> futureList = new ArrayList<>();
        for (int i = 0; i < boundaryList.size(); i++) {
            final int finalI = i;
            futureList.add(CompletableFuture.runAsync(() -> {
                Pair<Integer, Integer> boundary = boundaryList.get(finalI);
                log.info("--------开始执行第{}组，总共{}组------------", finalI + 1, boundaryList.size());
                List<CompanyGroup> subCompanyGroupList = companyGroupList.subList(boundary.getKey(), boundary.getValue());
                List<CompanyGroup> subBest = findSubBest(subCompanyGroupList, percent, allowBossNumBias);
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
            int allowBossNumBias
    ) {
        // 统计获取预期值
        int totalBossNum = 0; // 统计下boss总人数
        int totalChuFaBossNum = 0; //
        int totalPayBossNum = 0;
        int totalMoney = 0;
        for (CompanyGroup companyGroup : companyGroupList) {
            totalBossNum += companyGroup.getBossNum();
            totalChuFaBossNum += companyGroup.getChuFaBossNum();
            totalPayBossNum += companyGroup.getPayBossNum();
            totalMoney += companyGroup.getMoneyNum();
        }
        int bestBossNum = (int) Math.ceil(totalBossNum * percent);
        int minBossNum = bestBossNum - allowBossNumBias;
        int maxBossNum = bestBossNum + allowBossNumBias;
        double bestArpu = (double) totalMoney / totalChuFaBossNum;
        double bestPay = (double) totalPayBossNum / totalChuFaBossNum;
        double bestArppu = (double) totalMoney / totalPayBossNum;
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
        for (int i = 0; i < divideNum - 1; i++) {
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

    public static void main(String[] args) {
        // 配置项
        int maxCalcSecond = 60; // 程序运行时间
        double percent = 0.2; // 占比
        int allowBossNumBias = 20; // 允许的老板人数的偏差

        // 模拟数据
        long startMs = System.currentTimeMillis();
        List<CompanyGroup> companyGroupList = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 10_0000; i++) {
            int randomBossNum = random.nextInt(1000);
            int randomCompanyNum = random.nextInt(10);
            int randomChuFaBossNum = random.nextInt(20);
            int randomPayBossNum = random.nextInt(20);
            int randomMoneyNum = random.nextInt(1000);
            companyGroupList.add(new CompanyGroup(i, randomCompanyNum, randomBossNum, randomChuFaBossNum, randomPayBossNum, randomMoneyNum));
        }
        log.info("获取数据完毕，耗时{}ms", (System.currentTimeMillis() - startMs));

        syncFindBest(companyGroupList, percent, allowBossNumBias);

//        asyncFindBest(companyGroupList, percent, allowBossNumBias);

        log.info("总共耗时{}ms", (System.currentTimeMillis() - startMs));

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

        /**

         async 2线程 10_0000公司组 1000老板
         总共耗时397653ms = 397s = 6.6min

         sync 1线程 10_0000公司组 1000老板
         总共耗时647293ms = 647s = 10.7min

fixme: best数据按整体的，不要按局部的




         */

    }
}
