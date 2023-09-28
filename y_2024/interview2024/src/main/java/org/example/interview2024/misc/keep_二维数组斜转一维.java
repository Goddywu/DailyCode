package org.example.interview2024.misc;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2024-05-29
 * <p>
 * Given 一个二维数组
 * [ 1  11 15 21
 *   31 65 78 4
 *   97 11 18 63
 * ]
 * 将其展开成
// * [1 11 31 97 65 15 21 78 11 18 4 63]
 */
@Slf4j
public class keep_二维数组斜转一维 {

    public static int[] method1(int[][] numListList) {
        int colLen = numListList[0].length;
        int rowLen = numListList.length;
        int[] result = new int[colLen * rowLen];

        boolean flag = false;
        int count = 0;
        for (int sum = 0; sum <= colLen + rowLen - 2; sum++) {
            if (flag) {
                for (int i = 0; i <= sum; i++) {
                    if (i < rowLen && sum - i < colLen) {
                        result[count] = numListList[i][sum - i];
                        count++;
                    }
                }
            } else {
                for (int i = 0; i <= sum; i++) {
                    if (sum - i < rowLen && i < colLen) {
                        result[count] = numListList[sum - i][i];
                        count ++;
                    }
                }
            }
            flag = !flag;
        }
        return result;
    }


    public static void main(String[] args) {
        int[][] nums = new int[][]{{1, 11, 15, 21}, {31, 65, 78, 4}, {97, 11, 18, 63}};
        int[] ints = method1(nums);
        log.info("{}", ints);
    }
}
