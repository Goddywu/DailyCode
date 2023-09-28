package org.example.interview2024.misc;

import java.util.Arrays;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2024-06-24
 */
public class 元保科技_原地删除数组保留两次 {

    /*
    、给你一个有序数组 nums ，请你 原地 删除重复出现的元素，使得出现次数超过两次的元素只出现两次 ，返回删除后数组的新长度。

    不要使用额外的数组空间，你必须在 原地 修改输入数组 并在使用 O(1) 额外空间的条件下完成。
     */

    public static int method1(int[] nums) {
        if (nums.length <= 2) {
            return nums.length;
        }

        int idx = 2;
        for (int i = 2; i < nums.length; i++) {
            if (nums[i] != nums[idx - 1]) {
                nums[idx] = nums[i];
                idx++;
            } else if (nums[i] != nums[idx - 2]) {
                nums[idx] = nums[i];
                idx++;
            }
        }
        return idx;
    }

    public static void main(String[] args) {
        int[] nums = new int[]{1, 2, 2, 2, 2, 3, 3, 3};
        int i = method1(nums);
        System.out.println(Arrays.toString(nums));
        System.out.println(i);
    }
}
