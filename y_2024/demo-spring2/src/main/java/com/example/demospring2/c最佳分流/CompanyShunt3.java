package com.example.demospring2.c最佳分流;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Random;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2023-02-20
 */
@Slf4j
@Data
public class CompanyShunt3 {

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

    public static List<CompanyGroup> findBest(
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

        return null;
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



    private static List<List<CompanyGroup>> findAllCombinations(List<CompanyGroup> companyGroupList, int bestBossNum) {
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

        // 找到和为target的所有组合
        List<List<CompanyGroup>> result = new ArrayList<>();
        if (!dp.get(len).get(bestBossNum)) {
            return result;
        }

        for (int n = len; n >= 0; n--) {
            List<CompanyGroup> combination = new ArrayList<>();
            int remainingSum = bestBossNum;
            for (int i = n; i > 0 && remainingSum > 0; i--) {
                if (remainingSum >= companyGroupList.get(i - 1).getBossNum() && dp.get(i - 1).get(remainingSum - companyGroupList.get(i - 1).getBossNum()) && (i == 1 || companyGroupList.get(i - 2).getBossNum() != companyGroupList.get(i - 1).getBossNum())) {
                    combination.add(companyGroupList.get(i - 1));
                    remainingSum -= companyGroupList.get(i - 1).getBossNum();
                }
            }
            if (!combination.isEmpty()) {
                result.add(combination);
            }
        }
        return result;
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
        for (int i = 0; i < 1_000; i++) {
            int randomBossNum = random.nextInt(1000);
            int randomCompanyNum = random.nextInt(10);
            int randomChuFaBossNum = random.nextInt(20);
            int randomPayBossNum = random.nextInt(20);
            int randomMoneyNum = random.nextInt(1000);
            companyGroupList.add(new CompanyGroup(i, randomCompanyNum, randomBossNum, randomChuFaBossNum, randomPayBossNum, randomMoneyNum));
        }
        log.info("获取数据完毕，耗时{}ms", (System.currentTimeMillis() - startMs));

        findBest(companyGroupList, percent, allowBossNumBias);
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

         1_0000公司组，1000随机boss
         16:40:42.306 [main] INFO com.example.demospring2.c最佳分流.CompanyShuntFile - 获取数据完毕，耗时7ms
         16:40:42.318 [main] INFO com.example.demospring2.c最佳分流.CompanyShuntFile - 一共10000个公司组，最佳分配997927个老板
         16:42:01.683 [main] INFO com.example.demospring2.c最佳分流.CompanyShuntFile - 构建dp，耗时79364ms
         16:42:02.613 [main] INFO com.example.demospring2.c最佳分流.CompanyShuntFile - 采用组合7994个，更新最佳45次，耗时927ms
         16:42:02.613 [main] INFO com.example.demospring2.c最佳分流.CompanyShuntFile - [arpu] 最佳52.55554972062345，当前52.5603577521584
         16:42:02.616 [main] INFO com.example.demospring2.c最佳分流.CompanyShuntFile - [pay] 最佳1.002310633113473，当前1.002171328129039
         16:42:02.616 [main] INFO com.example.demospring2.c最佳分流.CompanyShuntFile - [arppu] 最佳52.434393075697876，当前52.44647923652308


         1_0000公司组，100随机boss
         17:09:29.022 [main] INFO com.example.demospring2.c最佳分流.CompanyShunt3 - 获取数据完毕，耗时9ms
         17:09:29.035 [main] INFO com.example.demospring2.c最佳分流.CompanyShunt3 - 一共10000个公司组，最佳分配99345个老板
         17:09:35.804 [main] INFO com.example.demospring2.c最佳分流.CompanyShunt3 - 构建dp，耗时6769ms
         17:09:36.210 [main] INFO com.example.demospring2.c最佳分流.CompanyShunt3 - 采用组合7935个，更新最佳52次，耗时403ms
         17:09:36.210 [main] INFO com.example.demospring2.c最佳分流.CompanyShunt3 - [arpu] 最佳53.24466737064414，当前53.24437486733178
         17:09:36.212 [main] INFO com.example.demospring2.c最佳分流.CompanyShunt3 - [pay] 最佳1.01220696937698，当前1.0121524092549352
         17:09:36.212 [main] INFO com.example.demospring2.c最佳分流.CompanyShunt3 - [arppu] 最佳52.60254965782006，当前52.60509620930111
         17:09:36.212 [main] INFO com.example.demospring2.c最佳分流.CompanyShunt3 - 总共耗时7204ms


         1000公司组，1000随机boss
         17:10:47.039 [main] INFO com.example.demospring2.c最佳分流.CompanyShunt3 - 获取数据完毕，耗时10ms
         17:10:47.046 [main] INFO com.example.demospring2.c最佳分流.CompanyShunt3 - 一共1000个公司组，最佳分配99910个老板
         17:10:47.917 [main] INFO com.example.demospring2.c最佳分流.CompanyShunt3 - 构建dp，耗时871ms
         17:10:47.950 [main] INFO com.example.demospring2.c最佳分流.CompanyShunt3 - 采用组合808个，更新最佳8次，耗时33ms
         17:10:47.952 [main] INFO com.example.demospring2.c最佳分流.CompanyShunt3 - [arpu] 最佳53.13375796178344，当前52.85736118186449
         17:10:47.954 [main] INFO com.example.demospring2.c最佳分流.CompanyShunt3 - [pay] 最佳0.9936305732484076，当前0.9831889964340296
         17:10:47.955 [main] INFO com.example.demospring2.c最佳分流.CompanyShunt3 - [arppu] 最佳53.47435897435897，当前53.76113989637306
         17:10:47.955 [main] INFO com.example.demospring2.c最佳分流.CompanyShunt3 - 总共耗时936ms


         */
    }
}
