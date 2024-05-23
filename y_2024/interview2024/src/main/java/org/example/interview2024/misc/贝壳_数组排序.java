package org.example.interview2024.misc;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2024-05-23
 */
@Slf4j
public class 贝壳_数组排序 {
    /*

    给定一个数组 nums，编写一个函数将所有 0 移动到数组的末尾，同时保持非零元素的相对顺序。
    ## 请注意 ，必须在不复制数组的情况下原地对数组进行操作。

    示例 1:
    输入: nums = [0,1,0,3,12]
    输出: [1,3,12,0,0]

    示例 2:
    输入: nums = [0]
    输出: [0]
     */

    public static void sort(int[] arr) {
        if (arr.length <= 1) {
            return;
        }

        Integer idx = null;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != 0) {
                if (idx != null) {
                    swap(arr, idx, i);
                    idx = idx + 1;
                }
            } else {
                if (idx == null) {
                    idx = i;
                }
            }
        }
    }

    public static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    public static void main(String[] args) {
        int[] arr1 = new int[]{0,0,1,3,12};
        sort(arr1);
        log.info("{}", arr1);

    }
}
