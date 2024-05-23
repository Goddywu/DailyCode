package com.example.demospring2.c最佳分流;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2023-02-14
 */
@Slf4j
@Data
public class Test2 {

    private int[] nums; // 数组
    private int K; // 使用的数字的数量
    private int S; // 目标的数据和

    private List<Integer> bestMarkIndexList;
    private Integer bestS;


    public void distribute() {
        bestMarkIndexList = new ArrayList<>();

        Stack<Task> taskStack = new Stack<>();

        Task task = new Task();
        task.setLeft(0);
        task.setRight(nums.length - 1);
        task.setK(K);
        task.setCurS(0);
        task.setMarkIndexList(new ArrayList<>());
        taskStack.add(task);

        while (taskStack.size() > 0) {
            Task popTask = taskStack.pop();
            int left = popTask.getLeft();
            int right = popTask.getRight();
            int k = popTask.getK();
            int curS = popTask.getCurS();
            List<Integer> tmpMarkIndexList = new ArrayList<>(popTask.getMarkIndexList());

            // 如果用了足够的数，比较下
            if (k == 0) {
                judgeBest(curS, tmpMarkIndexList);
                continue;
            }

            // 如果当前可用的数，正好等于目标数
            int canUseNum = right - left + 1;
            if (canUseNum == k) {
                int tmpS = curS;
                for (int i = left; i <= right; i++) {
                    tmpMarkIndexList.add(i);
                    tmpS += nums[i];
                }
                judgeBest(tmpS, tmpMarkIndexList);
                continue;
            }

            if (k > 2) {
                // 不用当前最后一个
                Task task1 = new Task();
                task1.setLeft(left);
                task1.setRight(right - 1);
                task1.setK(k);
                task1.setCurS(curS);
                task1.setMarkIndexList(tmpMarkIndexList);
                taskStack.add(task1);

                // 用当前最后一个
                List<Integer> tmpMarkIndexList2 = new ArrayList<>(popTask.getMarkIndexList());
                tmpMarkIndexList2.add(right);
                Task task2 = new Task();
                task2.setLeft(left);
                task2.setRight(right - 1);
                task2.setK(k - 1);
                task2.setCurS(curS + nums[right]);
                task2.setMarkIndexList(tmpMarkIndexList2);
                taskStack.add(task2);
                continue;
            }

            if (k == 2) {
                while (right > left) {
                    tmpMarkIndexList.add(left);
                    tmpMarkIndexList.add(right);
                    int tmpS = curS + nums[left] + nums[right];
                    judgeBest(tmpS, tmpMarkIndexList);
                    if (tmpS > S) {
                        tmpMarkIndexList.remove(tmpMarkIndexList.size() - 1);
                        tmpMarkIndexList.remove(tmpMarkIndexList.size() - 1);
                        right--;
                    } else if (tmpS < S) {
                        tmpMarkIndexList.remove(tmpMarkIndexList.size() - 1);
                        tmpMarkIndexList.remove(tmpMarkIndexList.size() - 1);
                        left++;
                    } else {
                        break;
                    }
                }
            }
        }

        log.info("--- best: " + bestS);
        List<Integer> ans = new ArrayList<>();
        for (int i = 0; i < bestMarkIndexList.size(); i++) {
            ans.add(nums[bestMarkIndexList.get(i)]);
        }
        log.info("--- nums: " + ans);
    }

    @Data
    private static class Task {
        private int left;
        private int right;
        private int k;
        private int curS;
        private List<Integer> markIndexList;
    }

    private void judgeBest(int tmpS, List<Integer> tmpMarkIndexList) {
        if (bestS == null || Math.abs(tmpS - S) < Math.abs(bestS - S)) {
            bestS = tmpS;
            bestMarkIndexList = new ArrayList<>(tmpMarkIndexList);
        }
    }

    public static void main(String[] args) {
        int[] nums = new int[]{1, 3, 5, 10, 12};
        Test2 test = new Test2();
        test.setNums(nums);
        test.setK(3);
        test.setS(12);

        test.distribute();

//        int[] nums = new int[]{1, 5, 8, 9, 17, 20};
//        Test2 test = new Test2();
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
