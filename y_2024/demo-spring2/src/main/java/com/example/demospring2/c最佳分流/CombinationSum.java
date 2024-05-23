package com.example.demospring2.c最佳分流;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class CombinationSum {

    // 错的

    public static List<List<Integer>> findCombinationSum(int[] candidates, int target) {
        Arrays.sort(candidates);
        int n = candidates.length;

        // 初始化dp数组
        int[][] dp = new int[n+1][target+1];
        dp[0][0] = 1;

        // 填充dp数组
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j <= target; j++) {
                if (j >= candidates[i-1]) {
                    dp[i][j] = dp[i-1][j] + dp[i][j-candidates[i-1]];
                } else {
                    dp[i][j] = dp[i-1][j];
                }
            }
        }

        // 回溯找到所有组合
        List<List<Integer>> result = new ArrayList<>();
        Stack<Node> stack = new Stack<>();
        stack.push(new Node(n, target, new ArrayList<>()));

        while (!stack.isEmpty()) {
            Node node = stack.pop();
            int index = node.index;
            int targetSum = node.targetSum;
            List<Integer> combination = node.combination;

            if (index == 0 && targetSum == 0) {
                result.add(combination);
                continue;
            }

            if (index <= 0 || targetSum < 0) {
                continue;
            }

            if (targetSum >= candidates[index-1] && dp[index][targetSum-candidates[index-1]] > 0) {
                List<Integer> newCombination = new ArrayList<>(combination);
                newCombination.add(candidates[index-1]);
                stack.push(new Node(index, targetSum-candidates[index-1], newCombination));
            }

            stack.push(new Node(index-1, targetSum, combination));
        }

        return result;
    }

    private static class Node {
        int index;
        int targetSum;
        List<Integer> combination;

        public Node(int index, int targetSum, List<Integer> combination) {
            this.index = index;
            this.targetSum = targetSum;
            this.combination = combination;
        }
    }

    public static void main(String[] args) {
        int[] candidates = {2, 3, 6, 7, 7, 8};
        int target = 15;
        List<List<Integer>> result = findCombinationSum(candidates, target);
        System.out.println(Arrays.toString(result.toArray()));
    }
}
