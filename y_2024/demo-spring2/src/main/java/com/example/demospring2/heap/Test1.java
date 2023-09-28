package com.example.demospring2.heap;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2023-02-21
 */
public class Test1 {

    public static void test1() {
//        boolean[][] matrix = new boolean[10000][100_0000];
        List<List<Boolean>> matrix2 = new ArrayList<>(100_0000);
        for (int i = 0; i < matrix2.size(); i++) {
            List<Boolean> list = new ArrayList<>(1_0000);

            matrix2.add(list);
        }

    }

    public static void main(String[] args) {
        test1();
    }
}
