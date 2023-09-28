package com.example.demospring2.c最佳分流;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Random;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2023-02-20
 */
@Slf4j
@Data
public class CompanyShunt {

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

        long startMs = System.currentTimeMillis();
        List<List<CompanyGroup>> allCombinations = findAllCombinations(companyGroupList, bestBossNum);
        log.info("找到{}个组合，耗时{}ms", allCombinations.size(), (System.currentTimeMillis() - startMs));

        startMs = System.currentTimeMillis();
        List<CompanyGroup> bestCompanyGroup = findBestCompanyGroup(allCombinations, bestArpu, bestPay, bestArppu);
        log.info("找到最佳组合，耗时{}ms", (System.currentTimeMillis() - startMs));

        return null;
    }

    private static List<CompanyGroup> findBestCompanyGroup(
            List<List<CompanyGroup>> allCombinations,
            double bestArpu,
            double bestPay,
            double bestArppu
    ) {
        Double betterScore = null;
        Double betterArpu = null;
        Double betterPay = null;
        Double betterArppu = null;
        List<CompanyGroup> betterCompanyGroup = null;
        for (List<CompanyGroup> combination : allCombinations) {
            int totalChuFaBossNum = 0; //
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
            double score = -(Math.abs(bestArpu - arpu) + Math.abs(bestPay - pay) + Math.abs(bestArppu - arppu));
            if (betterScore == null || score > betterScore) {
                betterScore = score;
                betterCompanyGroup = combination;
                betterArpu = arpu;
                betterPay = pay;
                betterArppu = arppu;
            }
        }
        log.info("[arpu] 最佳{}，当前{}", bestArpu, betterArpu);
        log.info("[pay] 最佳{}，当前{}", bestPay, betterPay);
        log.info("[arppu] 最佳{}，当前{}", bestArppu, betterArppu);
        return betterCompanyGroup;
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
        List<CompanyGroup> companyGroupList = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 1_0000; i++) {
            int randomBossNum = random.nextInt(10);
            int randomCompanyNum = random.nextInt(10);
            int randomChuFaBossNum = random.nextInt(20);
            int randomPayBossNum = random.nextInt(20);
            int randomMoneyNum = random.nextInt(1000);
            companyGroupList.add(new CompanyGroup(i, randomCompanyNum, randomBossNum, randomChuFaBossNum, randomPayBossNum, randomMoneyNum));
        }
        log.info("获取数据完毕");

        findBest(companyGroupList, percent, allowBossNumBias);

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
    }
}
