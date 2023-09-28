package com.example.demospring2.c最佳分流;

import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2023-02-14
 */
@Data
public class Test3 {

    private int[] nums; // 数组
    private int K; // 使用的数字的数量

    public void distribute() {
        int sum = 0;
        for (int i = 0; i < nums.length; i++) {
            sum += nums[i];
        }
        List<Integer> useList = new ArrayList<>();
        List<Integer> bigList = new ArrayList<>();
        List<Integer> smallList = new ArrayList<>();
        double average = (double) sum / nums.length;
        for (int i = 0; i < nums.length; i++) {
            if (useList.size() == K) {
                break;
            }
            if (nums[i] > average) {
                bigList.add(i);
            } else if (nums[i] < average) {
                smallList.add(i);
            } else {
                useList.add(i);
            }
        }
        bigList.sort(Comparator.reverseOrder());
        smallList.sort(Integer::compareTo);
        double bias = 0;
        while (useList.size() < K) {
            int index;
            if (!smallList.isEmpty() && bias >= 0) {
                index = smallList.remove(0);
            } else {
                index = bigList.remove(0);
            }
            useList.add(index);
            bias += nums[index] - average;
        }

    }

    public static void main(String[] args) {
        int[] nums = new int[]{1, 3, 5, 10, 12};
        Test3 test = new Test3();
        test.setNums(nums);
        test.setK(3);

        test.distribute();

    }
}
