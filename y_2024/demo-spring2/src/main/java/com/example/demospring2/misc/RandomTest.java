package com.example.demospring2.misc;

import java.util.Random;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2023-08-24
 */
public class RandomTest {


    public static void test1() {
        for (int i = 0; i < 10; i++) {

            Random random = new Random();
            int zeroOrOne = random.nextInt(2);
            System.out.println(zeroOrOne);
        }
    }

    public static void main(String[] args) {
        test1();
    }
}
