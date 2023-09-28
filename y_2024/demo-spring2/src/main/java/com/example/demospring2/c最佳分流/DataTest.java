package com.example.demospring2.c最佳分流;

import java.util.Random;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2023-02-14
 */
public class DataTest {

    public static void test1() {
        int[] nums = new int[]{1, 3, 5, 10, 12};
        int k = 3;
        int s = 12;

        System.out.println("==== 1 ====");
        Test1 test1 = new Test1();
        test1.setNums(nums);
        test1.setK(k);
        test1.setS(s);
        test1.distribute();
        System.out.println("==== 2 ====");
        Test2 test2 = new Test2();
        test2.setNums(nums);
        test2.setK(k);
        test2.setS(s);
        test2.distribute();

    }

    public static void test2() {
        int[] nums = new int[1_0000];
        Random random = new Random();
        for (int i = 0; i < nums.length; i++) {
            nums[i] = random.nextInt(100_0000);
        }
        int k = 2000;
        int s = 100000;

//        System.out.println("==== 1 ====");
//        Test1 test1 = new Test1();
//        test1.setNums(nums);
//        test1.setK(k);
//        test1.setS(s);
//        test1.distribute();
        System.out.println("==== 2 ====");
        Test2 test2 = new Test2();
        test2.setNums(nums);
        test2.setK(k);
        test2.setS(s);
        test2.distribute();
    }

    public static void main(String[] args) {
//        test1();
        test2();
    }
}
