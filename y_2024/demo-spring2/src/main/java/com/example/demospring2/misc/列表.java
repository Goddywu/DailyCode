package com.example.demospring2.misc;

import cn.hutool.core.lang.Pair;

import java.util.*;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2024-06-21
 */
public class 列表 {

    public static void test1() {
        List<Pair<Double, String>> list = new ArrayList<>();
        list.add(Pair.of(3d, "3"));
        list.add(Pair.of(1d, "1"));
        list.add(Pair.of(2d, "2"));

        Optional<String> s = list.stream().min((r1, r2) -> {
            double s1 = r1.getKey();
            double s2 = r2.getKey();
            return Double.compare(s2, s1);
        }).map(Pair::getValue);
        System.out.println(s);

        Optional<String> ss = list.stream().max(Comparator.comparingDouble(Pair::getKey)).map(Pair::getValue);
        System.out.println(ss);
    }

    public static void test2() {
        List<String> matchPriority = Arrays.asList("exact", "approximate", "regEx");

        System.out.println(matchPriority.indexOf("approximate"));
    }

    public static void main(String[] args) {
        test2();
    }
}
