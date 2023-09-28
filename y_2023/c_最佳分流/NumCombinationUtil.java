package c_最佳分流;

import cn.hutool.core.lang.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class NumCombinationUtil {


    /**
     * 按比例分组
     * @param nums 数量
     * @param percents 和为1000
     * @return nums的索引
     */
    public static List<List<Integer>> splitNumsByPercents(List<Integer> nums, List<Integer> percents) {
        List<List<Integer>> ans = new ArrayList<>();

        List<Pair<Integer, Integer>> tmpNumExtraList = new ArrayList<>();
        for (int i = 0; i < nums.size(); i++) {
            tmpNumExtraList.add(Pair.of(nums.get(i), i));
        }

        int totalPercent = percents.stream().reduce(Integer::sum).orElse(0);
        for (int i = 0; i < percents.size() - 1; i++) {
            int percent = percents.get(i);

            double calcPercent = (double) percent * 1000 / totalPercent;
            List<Integer> curIdxList = new ArrayList<>();
            List<Integer> tmpNums = tmpNumExtraList.stream().map(Pair::getKey).collect(Collectors.toList());

            List<Integer> idxList = splitNumsByPercent(tmpNums, calcPercent);
            if (idxList.isEmpty()) {
                ans.add(new ArrayList<>());
                continue;
            }
            for (int idx : idxList) {
                curIdxList.add(tmpNumExtraList.get(idx).getValue());
                tmpNumExtraList.remove(idx);
            }
            ans.add(curIdxList);

            totalPercent -= percent;
        }
        if (!tmpNumExtraList.isEmpty()) {
            ans.add(tmpNumExtraList.stream().map(Pair::getValue).collect(Collectors.toList()));
        }
        return ans;
    }

    /**
     * 按比例分组
     * @param nums 数量
     * @param percent 千分之几
     * @return nums的索引
     */
    public static List<Integer> splitNumsByPercent(List<Integer> nums, double percent) {
        int total = 0;
        int[] candidates = new int[nums.size()];
        for (int i = 0; i < nums.size(); i++) {
            total += nums.get(i);
            candidates[i] = nums.get(i);
        }
        int targetNum = (int) Math.floor(total * percent / 1000d);
        return findOneCombinationSum(candidates, targetNum, Integer.MAX_VALUE)
                .orElse(new ArrayList<>());
    }

    /**
     * @return 符合的索引
     */
    public static List<List<Integer>> findCombinationSum(int[] candidates, int target) {
        boolean[][] dp = buildDp(candidates, target);
        return findAllResult(candidates, target, dp);
    }

    /**
     * @return 符合的索引
     */
    public static Optional<List<Integer>> findOneCombinationSum(int[] candidates, int target) {
        boolean[][] dp = buildDp(candidates, target);
        return findOneResult(candidates, target, dp);
    }


    /**
     * @param allowBias 允许的偏差
     * @return 符合的索引
     */
    public static Optional<List<Integer>> findOneCombinationSum(int[] candidates, int target, int allowBias) {
        boolean[][] dp = buildDp(candidates, target);
        Optional<List<Integer>> optional = findOneResult(candidates, target, dp);
        if (optional.isPresent()) {
            return optional;
        }

        for (int i = 1; i <= allowBias; i++) {
            // 尝试使用大一点的
            optional = findOneResult(candidates, target + i, dp);
            if (optional.isPresent()) {
                return optional;
            }

            // 尝试使用小一点的
            if (target - i < 0) {
                continue;
            }
            optional = findOneResult(candidates, target - i, dp);
            if (optional.isPresent()) {
                return optional;
            }
        }

        return Optional.empty();
    }


    // ==========================================================================
    // private
    // ==========================================================================


    private static boolean[][] buildDp(int[] candidates, int target) {
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
        return dp;
    }

    /**
     * @return 符合的索引
     */
    private static Optional<List<Integer>> findOneResult(int[] candidates, int target, boolean[][] dp) {
        int n = candidates.length;
        if (target >= dp[n].length) {
            return Optional.empty();
        }
        if (dp[n][target]) {
            List<Integer> combination = new ArrayList<>();
            int remainingSum = target;
            for (int i = n; i > 0 && remainingSum > 0; i--) {
                if (remainingSum >= candidates[i - 1] && dp[i - 1][remainingSum - candidates[i - 1]] && (i == 1 || candidates[i - 2] != candidates[i - 1])) {
                    combination.add(i - 1);
                    remainingSum -= candidates[i - 1];
                }
            }
            return Optional.of(combination);
        }
        return Optional.empty();
    }

    /**
     * @return 符合的索引
     */
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
                    combination.add(i - 1);
                    remainingSum -= candidates[i - 1];
                }
            }
            if (!combination.isEmpty()) {
                result.add(combination);
            }
        }

        return result;
    }

}
