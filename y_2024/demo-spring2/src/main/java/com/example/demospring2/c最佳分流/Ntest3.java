package com.example.demospring2.c最佳分流;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2023-02-20
 */
public class Ntest3 {


    public static List<List<Integer>> findCombinationSum(int[] candidates, int target) {
        int n = candidates.length;

        // 初始化dp数组
        boolean[][] dp = new boolean[n + 1][target + 1];
        dp[0][0] = true;
        for (int i = 1; i <= n; i++) {
            dp[i][0] = true;
        }

        // 填充dp数组
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= target; j++) {
                if (j < candidates[i - 1]) {
                    dp[i][j] = dp[i - 1][j];
                } else {
                    dp[i][j] = dp[i - 1][j] || (i == 1 || candidates[i - 2] != candidates[i - 1]) && dp[i - 1][j - candidates[i - 1]];
                }
            }
        }

        // 找到和为target的所有组合
//        List<List<Integer>> result = findOneResult(candidates, target, dp);
        List<List<Integer>> result = findAllResult(candidates, target, dp);

        return result;
    }

    private static List<List<Integer>> findOneResult(int[] candidates, int target, boolean[][] dp) {
        int n = candidates.length;
        List<List<Integer>> result = new ArrayList<>();
        if (dp[n][target]) {
            List<Integer> combination = new ArrayList<>();
            int remainingSum = target;
            for (int i = n; i > 0 && remainingSum > 0; i--) {
                if (remainingSum >= candidates[i - 1] && dp[i - 1][remainingSum - candidates[i - 1]] && (i == 1 || candidates[i - 2] != candidates[i - 1])) {
                    combination.add(candidates[i - 1]);
                    remainingSum -= candidates[i - 1];
                }
            }
            result.add(combination);
        }
        return result;
    }

    private static List<List<Integer>> findAllResult(int[] candidates, int target, boolean[][] dp) {
        List<List<Integer>> result = new ArrayList<>();
        if (!dp[candidates.length][target]) {
            return result;
        }

        for (int n = candidates.length; n >= 0; n--) {
            List<Integer> combination = new ArrayList<>();
            int remainingSum = target;
            for (int i = n; i > 0 && remainingSum > 0; i--) {
                if (remainingSum >= candidates[i - 1] && dp[i - 1][remainingSum - candidates[i - 1]] && (i == 1 || candidates[i - 2] != candidates[i - 1])) {
                    combination.add(candidates[i - 1]);
                    remainingSum -= candidates[i - 1];
                }
            }
            if (!combination.isEmpty()) {
                result.add(combination);
            }
        }

        return result;
    }


    private static List<List<Integer>> findAllResult2(int[] candidates, int target, boolean[][] dp) {
        List<List<Integer>> result = new ArrayList<>();
        if (!dp[candidates.length][target]) {
            return result;
        }

        for (int n = candidates.length; n >= 0; n--) {
            List<Integer> combination = new ArrayList<>();
            int remainingSum = target;
            for (int i = n; i > 0 && remainingSum > 0; i--) {
                if (remainingSum >= candidates[i - 1] && dp[i - 1][remainingSum - candidates[i - 1]] && (i == 1 || candidates[i - 2] != candidates[i - 1])) {
                    combination.add(candidates[i - 1]);
                    remainingSum -= candidates[i - 1];
                }
            }
            if (!combination.isEmpty()) {
                result.add(combination);
            }
        }

        return result;
    }



    public static void test1() {
        int[] candidates = {2, 3, 6, 7, 7, 8, 2, 11, 2, 1, 8};
        int target = 15;
        List<List<Integer>> result = findCombinationSum(candidates, target);
        System.out.println(Arrays.toString(result.toArray()));
    }

    public static void test2() {
        List<Integer> candidates = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 10000; i++) {
            candidates.add(random.nextInt(100));
        }

        long startMs = System.currentTimeMillis();
        List<List<Integer>> result = findCombinationSum(candidates.stream().mapToInt(Integer::intValue).toArray(), 10000);
        System.out.println("--------------");
        System.out.println("总共: " + result.size() + "个组合，耗时: " + (System.currentTimeMillis() - startMs) + "ms");
        for (int i = 0; i < 3 && i < result.size(); i++) {
            System.out.println("--------------");
            System.out.println(result.get(i));
        }
    }

    public static void main(String[] args) {
//        test1();
        test2();
    }
}
