package com.example.demospring2.misc;

import java.util.Random;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2024-02-26
 */
public class 随机数 {

    public static void main(String[] args) {
        for (int j = 0; j < 30; j++) {
            StringBuilder sb = new StringBuilder();
            Random random = new Random();
            for (int i = 0; i < 6; i++) {
                sb.append((char) (random.nextInt('9' - '0') + '0'));
            }
            System.out.println(sb.toString());
        }
    }
}
