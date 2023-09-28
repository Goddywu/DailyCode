package com.example.demospring2.misc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2023-08-03
 */
public class BooleanTest {

    public static void permutation(List<Integer> list, int start, List<List<Integer>> resList) {
        if (start == list.size()) {
            resList.add(new ArrayList<>(list));
            return;
        }

        for (int i = start; i < list.size(); i++) {
            Collections.swap(list, start, i);
            permutation(list, start + 1, resList);
            Collections.swap(list, start, i);
        }
    }


    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        List<List<Integer>> resList = new ArrayList<>();

        permutation(list, 0, resList);

        System.out.println(resList);
    }

}
