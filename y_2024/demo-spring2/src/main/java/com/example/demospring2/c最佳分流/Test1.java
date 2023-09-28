package com.example.demospring2.c最佳分流;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2023-02-13
 *
 * 递归
 */
@Slf4j
@Data
public class Test1 {

    private int[] nums; // 数组
    private int K; // 使用的数字的数量
    private int S; // 目标的数据和

    private boolean[] useMark;
    private boolean[] bestMark;
    private Integer bestS;

    public void distribute() {
        useMark = new boolean[nums.length];
        bestMark = new boolean[nums.length];

        distribute(0, nums.length - 1, K, 0);

        log.info("--- best: " + bestS);
        List<Integer> ans = new ArrayList<>();
        for (int i = 0; i < bestMark.length; i++) {
            if (bestMark[i]) {
                ans.add(nums[i]);
            }
        }
        log.info("--- nums: " + ans);
    }

    /**
     *
     * @param left 可用的左边界
     * @param right 可用的右边界
     * @param k 需要用的数
     * @param curS 当前使用数的和
     */
    private void distribute(int left, int right, int k, int curS) {
        // 如果用了足够的数，比较下
        if (k == 0) {
            judgeBest(curS);
            return;
        }

        // 如果当前可用的数，正好等于目标数
        int canUseNum = right - left + 1;
        if (canUseNum == k) {
            int tmpS = curS;
            for (int i = left; i <= right; i++) {
                useMark[i] = true;
                tmpS += nums[i];
            }
            judgeBest(tmpS);
            for (int i = 0; i <= right; i++) {
                useMark[i] = false;
            }
            return;
        }

        if (k > 2) {
            // 不用当前最后一个
            distribute(left, right - 1, k, curS);
            // 用当前最后一个
            useMark[right] = true;
            distribute(left, right - 1, k - 1, curS + nums[right]);
            useMark[right] = false;
            return;
        }

        if (k == 2) {
            while (right > left) {
                useMark[left] = true;
                useMark[right] = true;
                int tmpS = curS + nums[left] + nums[right];
                judgeBest(tmpS);
                if (tmpS > S) {
                    useMark[left] = false;
                    useMark[right] = false;
                    right--;
                } else if (tmpS < S) {
                    useMark[left] = false;
                    useMark[right] = false;
                    left++;
                } else {
                    break;
                }
            }
        }
    }

    private void judgeBest(int tmpS) {
        if (bestS == null || Math.abs(tmpS - S) < Math.abs(bestS - S)) {
            bestS = tmpS;
            System.arraycopy(useMark, 0, bestMark, 0, nums.length);
        }
    }

    public static void main(String[] args) {
        int[] nums = new int[]{1, 3, 5, 10, 12};
        Test1 test = new Test1();
        test.setNums(nums);
        test.setK(3);
        test.setS(12);

        test.distribute();

//        int[] nums = new int[]{1, 5, 8, 9, 17, 20};
//        Test1 test = new Test1();
//        test.setNums(nums);
//        test.setK(4);
//        test.setS(10);
//
//        test.distribute();


        // ⚠️ Exception in thread "main" java.lang.StackOverflowError
//        int[] nums = new int[1_0000];
//        Random random = new Random();
//        for (int i = 0; i < nums.length; i++) {
//            nums[i] = random.nextInt(100_0000);
//        }
//
//        Test1 test = new Test1();
//        test.setNums(nums);
//        test.setK(2000);
//        test.setS(100000);
//
//        test.distribute();

    }
}
