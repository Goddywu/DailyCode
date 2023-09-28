package com.example.demospring2.list;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2023-03-08
 */
public class Test1 {

    private static List<Integer> generateListOf1(int size) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(1);
        }
        return list;
    }


    public static void main(String[] args) {
//        System.out.println(generateListOf1(0));
//        System.out.println(generateListOf1(1));
//        System.out.println(generateListOf1(2));
//        System.out.println(generateListOf1(3));

        String[] a = new String[] {"1", "2"};
        List<String> b = Arrays.asList(a);
        System.out.println(111);
    }

}
