package com.example.demospring2.misc;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;


/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2024-04-24
 */
public class LinkedHashMap测试 {

    public static void main(String[] args) {
        LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
        map.put("c", 3);
        map.put("a", 1);
        map.put("b", 2);
        System.out.println(map);

        for (Map.Entry<String, Integer> e : map.entrySet()) {

            System.out.println("k: " + e.getKey() + ", v: " + e.getValue());

        }

        LinkedList<Map.Entry<String, Integer>> entryList = new LinkedList<>(map.entrySet());
        while (!entryList.isEmpty()) {
            System.out.println(entryList.pollLast());
        }

    }
}
