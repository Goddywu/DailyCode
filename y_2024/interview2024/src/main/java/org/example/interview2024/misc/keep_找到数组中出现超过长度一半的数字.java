package org.example.interview2024.misc;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2024-06-05
 */
public class keep_找到数组中出现超过长度一半的数字 {

    public static Integer method1(int[] array) {
        Map<Integer, Integer> map = new HashMap<>();
        int max = 0;
        Integer best = null;
        for (int j : array) {
            Integer compute = map.compute(j, (k, v) -> v == null ? 1 : v + 1);
            if (max == 0 || compute > max) {
                max = compute;
                best = j;
            }
        }
        if (max >= (array.length + 1) / 2) {
            return best;
        } else {
            return null;
        }
    }

    public static void main(String[] args) {
        System.out.println("---method1----");
        System.out.println(method1(new int[]{1, 2, 2, 2, 5}));
        System.out.println(method1(new int[]{1, 2, 3, 2, 5}));
        System.out.println("---method2----");
    }
}
