package com.example.demospring2.c最佳分流;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2023-02-17
 */
public class Ntest2 {

    @Data
    @AllArgsConstructor
    private static class CompanyGroup {

        private int companyGroupId;
        private int companyNum;
        private int bossNum;
        private int chuFaBossNum;
        private int payBossNum;
        private int moneyNum;
    }

    @Data
    private static class Situation {
        private List<CompanyGroup> companyGroupList;
        private int left = 0;
        private int right = 0;
        private int totalBossNum = 0;
        private int totalChuFaBossNum = 0;
        private int totalPayBossNum = 0;
        private int totalMoney = 0;

        private double expArpu = 0;
        private double expPay = 0;
        private double expArppu = 0;
        private double score = -Double.MAX_VALUE;

        private void updateExpAndScore(double bestExpArpu, double bestExpPay, double bestExpArppu) {
            this.expArpu = (double) totalMoney / totalChuFaBossNum;
            this.expPay = (double) totalPayBossNum / totalChuFaBossNum;
            this.expArppu = (double) totalMoney / totalPayBossNum;

            this.score = -(Math.abs(bestExpArpu - expArpu) + Math.abs(bestExpPay - expPay) + Math.abs(bestExpArppu - expArppu));
        }
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
            int randomBossNum = random.nextInt(100);
            int randomCompanyNum = random.nextInt(10);
            int randomChuFaBossNum = random.nextInt(20);
            int randomPayBossNum = random.nextInt(20);
            int randomMoneyNum = random.nextInt(1000);
            companyGroupList.add(new CompanyGroup(i, randomCompanyNum, randomBossNum, randomChuFaBossNum, randomPayBossNum, randomMoneyNum));
        }

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
        int minBossNum = (int) Math.ceil(totalBossNum * percent) - allowBossNumBias;
        int maxBossNum = (int) Math.floor(totalBossNum * percent) + allowBossNumBias;
        double bestExpArpu = (double) totalMoney / totalChuFaBossNum;
        double bestExpPay = (double) totalPayBossNum / totalChuFaBossNum;
        double bestExpArppu = (double) totalMoney / totalPayBossNum;

        // 滑动窗口找到最合适的
        int left = 0;
        int right = 0;
        int curBossSum = companyGroupList.get(0).getBossNum();
        int curChuFaBossSum = companyGroupList.get(0).getChuFaBossNum();
        int curPayBossNum = companyGroupList.get(0).getPayBossNum();
        int curMoney = companyGroupList.get(0).getMoneyNum();
        Situation bestSituation = new Situation();
        bestSituation.setCompanyGroupList(companyGroupList);

        long startMs = System.currentTimeMillis();
        int updateBest = 0;
        while (
                (System.currentTimeMillis() - startMs) <= maxCalcSecond * 1000
                        && right < companyGroupList.size() - 1
        ) {
            if (curBossSum < minBossNum) {
                right++;
                curBossSum += companyGroupList.get(right).getBossNum();
                curChuFaBossSum += companyGroupList.get(right).getChuFaBossNum();
                curPayBossNum += companyGroupList.get(right).getPayBossNum();
                curMoney += companyGroupList.get(0).getMoneyNum();
            } else if (curBossSum > maxBossNum) {
                curBossSum -= companyGroupList.get(left).getBossNum();
                curChuFaBossSum -= companyGroupList.get(left).getChuFaBossNum();
                curPayBossNum -= companyGroupList.get(right).getPayBossNum();
                curMoney -= companyGroupList.get(0).getMoneyNum();
                left++;
            } else {
                double expArpu = (double) curMoney / curChuFaBossSum;
                double expPay = (double) curPayBossNum / curChuFaBossSum;
                double expArppu = (double) curMoney / curPayBossNum;
                double curBestScore = bestSituation.getScore();
                double curScore = -(Math.abs(bestExpArpu - expArpu) + Math.abs(bestExpPay - expPay) + Math.abs(bestExpArppu - expArppu));
                if (curScore > curBestScore) {
                    bestSituation.setLeft(left);
                    bestSituation.setRight(right);
                    bestSituation.setTotalBossNum(curBossSum);
                    bestSituation.setTotalChuFaBossNum(curChuFaBossSum);
                    bestSituation.setTotalPayBossNum(curPayBossNum);
                    bestSituation.setTotalMoney(curMoney);
                    bestSituation.updateExpAndScore(bestExpArpu, bestExpPay, bestExpArppu);
                    updateBest++;
                }

                right++;
                curBossSum += companyGroupList.get(right).getBossNum();
                curChuFaBossSum += companyGroupList.get(right).getChuFaBossNum();
                curPayBossNum += companyGroupList.get(right).getPayBossNum();
                curMoney += companyGroupList.get(0).getMoneyNum();
            }
        }




        System.out.println("更新最佳次数: " + updateBest);


    }

}
